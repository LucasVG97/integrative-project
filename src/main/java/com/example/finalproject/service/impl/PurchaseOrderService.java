package com.example.finalproject.service.impl;

import com.example.finalproject.exception.NotFoundException;
import com.example.finalproject.exception.PurchaseFailureException;
import com.example.finalproject.exception.QuantityNotAvailableException;
import com.example.finalproject.model.*;
import com.example.finalproject.model.Enum.OrderStatus;
import com.example.finalproject.repository.*;
import com.example.finalproject.service.IPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService implements IPurchaseOrderService {

    @Autowired
    private PurchaseOrderRepo purchaseOrderRepo;

    @Autowired
    private PurchaseItemRepo purchaseItemRepo;

    @Autowired
    private AdvertisementRepo advertisementRepo;

    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private BatchRepo batchRepo;

    @Autowired
    private SectionRepo sectionRepo;

    @Autowired
    private SellerRepo sellerRepo;


    @Override
    public BigDecimal createPurchaseOrder(PurchaseOrder purchaseOrder) {
        buyerRepo.findById(purchaseOrder.getBuyer().getBuyerCode()).orElseThrow(() -> new NotFoundException("Buyer not found"));
        BigDecimal totalPrice = new BigDecimal(0);
        LocalDate dateTime = LocalDate.of(purchaseOrder.getDateTime().getYear(), purchaseOrder.getDateTime().getMonth(), purchaseOrder.getDateTime().getDayOfMonth());

        List<PurchaseItem> purchaseItemList = purchaseOrder.getPurchaseItems();
        for (PurchaseItem purchaseItem : purchaseItemList) {
            Long code = purchaseItem.getAdvertisement().getAdvertisementCode();
            Advertisement advertisement = advertisementRepo.findById(code).orElseThrow(() -> new NotFoundException("Advertisement not found"));

            purchaseItem.setAdvertisement(advertisement);

            quantityValidation(dateTime, purchaseItem, advertisement);

            purchaseItem.setPrice(advertisement.getPrice().multiply(BigDecimal.valueOf(purchaseItem.getQuantity())));
            totalPrice = totalPrice.add(advertisement.getPrice().multiply(BigDecimal.valueOf(purchaseItem.getQuantity())));
        }
        purchaseOrderRepo.save(purchaseOrder);
        purchaseItemList.forEach(i -> i.setPurchaseOrder(purchaseOrder));
        purchaseItemRepo.saveAll(purchaseItemList);
        return totalPrice;
    }

    @Override
    public PurchaseOrder findAllAdvertisementsByPurchase(Long purchaseCode) {
        PurchaseOrder purchaseOrder = (purchaseOrderRepo.findById(purchaseCode).orElseThrow(() -> new NotFoundException("Purchase order not found")));
        purchaseOrder.getPurchaseItems().forEach(i -> i.getAdvertisement().setPrice(i.getPrice().divide(BigDecimal.valueOf(i.getQuantity()), RoundingMode.DOWN)));
        return purchaseOrder;
    }

    @Override
    public PurchaseOrder updatePurchaseStatus(Long purchaseCode) {
        PurchaseOrder purchaseOrder = purchaseOrderRepo.findById(purchaseCode).orElseThrow(() -> new NotFoundException("Purchase order not found"));
        if (purchaseOrder.getOrderStatus().equals(OrderStatus.FINALIZADO))
            throw new PurchaseFailureException("The purchase order is already finished");

        updateVolumeAndQuantity(purchaseOrder);
        purchaseOrder.setDateTime(LocalDateTime.now());
        purchaseOrder.setOrderStatus(OrderStatus.FINALIZADO);
        addSalesToSeller(purchaseOrder);
        return purchaseOrderRepo.save(purchaseOrder);
    }

    private void updateVolumeAndQuantity(PurchaseOrder purchaseOrder) {
        LocalDate dateTime = LocalDate.now();
        for (PurchaseItem purchaseItem : purchaseOrder.getPurchaseItems()) {
            int boughtQuantity = purchaseItem.getQuantity();
            List<Batch> batchList = getSortedAndFilteredBatchList(dateTime, purchaseItem);

            int totalQuantity = batchList.stream().mapToInt(Batch::getProductQuantity).sum();
            batchQuantityValidation(purchaseOrder, purchaseItem, boughtQuantity, totalQuantity);

            for (Batch batch : batchList) {
                int quantityDiff = boughtQuantity - batch.getProductQuantity();
                if (quantityDiff >= 0) {
                    boughtQuantity = setQuantityAndVolumeWhenNotLastBatch(boughtQuantity, batch);
                } else {
                    setQuantityAndVolumeWhenLastBatch(boughtQuantity, batch, quantityDiff);
                    break;
                }
            }
        }
    }

    private void setQuantityAndVolumeWhenLastBatch(int boughtQuantity, Batch batch, int quantityDiff) {
        Section section = batch.getInboundOrder().getSection();
        Float sectionVolume = section.getAccumulatedVolume();

        Float advertisementVolume = batch.getUnitVolume() * boughtQuantity;
        section.setAccumulatedVolume(sectionVolume - advertisementVolume);
        sectionRepo.save(section);

        batch.setProductQuantity(Math.abs(quantityDiff));
        batch.setVolume(batch.getVolume() - advertisementVolume);
        batchRepo.save(batch);
    }

    private int setQuantityAndVolumeWhenNotLastBatch(int boughtQuantity, Batch batch) {
        Section section = batch.getInboundOrder().getSection();
        Float sectionVolume = section.getAccumulatedVolume();

        section.setAccumulatedVolume(sectionVolume - batch.getVolume());
        sectionRepo.save(section);

        boughtQuantity -= batch.getProductQuantity();
        batch.setProductQuantity(0);
        batch.setVolume(0F);
        batchRepo.save(batch);
        return boughtQuantity;
    }

    private void batchQuantityValidation(PurchaseOrder purchaseOrder, PurchaseItem purchaseItem, int boughtQuantity, int totalQuantity) {
        if (totalQuantity < boughtQuantity) {
            purchaseOrder.setOrderStatus(OrderStatus.INDISPONIVEL);
            purchaseOrderRepo.save(purchaseOrder);
            throw new PurchaseFailureException("Your cart product quantity: " + purchaseItem.getAdvertisement().getName() + " has expired. Available quantity: " + totalQuantity);
        }
    }

    private void quantityValidation(LocalDate dateTime, PurchaseItem purchaseItem, Advertisement advertisement) {
        int totalQuantity = advertisement.getBatches().stream()
                .filter(b -> ChronoUnit.WEEKS.between(dateTime, b.getDueDate()) >= 3)
                .mapToInt(Batch::getProductQuantity).sum();

        if (totalQuantity < purchaseItem.getQuantity()) {
            throw new QuantityNotAvailableException("Quantity not available for product: " + advertisement.getName() + ". Available quantity: " + totalQuantity);
        }
    }

    private List<Batch> getSortedAndFilteredBatchList(LocalDate dateTime, PurchaseItem purchaseItem) {
        return purchaseItem.getAdvertisement().getBatches().stream()
                .filter(b -> ChronoUnit.WEEKS.between(dateTime, b.getDueDate()) >= 3)
                .sorted(Comparator.comparing(Batch::getDueDate))
                .collect(Collectors.toList());
    }

    private void addSalesToSeller(PurchaseOrder purchaseOrder){
        List<Seller> sellers = new ArrayList<>();
        for (PurchaseItem purchaseItem : purchaseOrder.getPurchaseItems()) {
            sellers.add(purchaseItem.getAdvertisement().getSeller());
        }
        for (Seller seller: sellerRepo.findAll()) {
            for (Seller value : sellers) {
                if (seller == value) {
                    seller.setSales(seller.getSales() + 1);
                    seller.setRating(getSellerRating(seller.getSales()));
                }
            }
        }
        sellerRepo.saveAll(sellers);
    }

    private String getSellerRating(Long sales){
        if(sales <= 5) return "beginner";
        else if(sales > 15) return "trusted";
        else return "average";
    }
}

