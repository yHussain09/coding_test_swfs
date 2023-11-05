package com.smallworld;

import com.smallworld.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDataFetcherTest {
    private TransactionDataFetcher dataFetcher;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        // Create a list of transactions for testing
        transactions = new ArrayList<>();
        transactions.add(new Transaction(663458L, BigDecimal.valueOf(430.2), "Tom Shelby",22, "Alfie Solomons",33, 1, false, "Looks like money laundering"));
        transactions.add(new Transaction(1284564L, BigDecimal.valueOf(150.2), "Aunt Polly",22, "Arthur Shelby",60, 2, true, "Never gonna give you up"));
        transactions.add(new Transaction(1284564L, BigDecimal.valueOf(150.2), "Billy Kimber",22, "Grace Burgess",60, 3, false, "Looks like money laundering"));

        // Create the data fetcher with the test transactions
        dataFetcher = new TransactionDataFetcher(transactions);
    }

    @Test
    void testGetTotalTransactionAmount() {
        assertEquals(730.6, Math.nextUp(dataFetcher.getTotalTransactionAmount()));
    }

    @Test
    void testGetTotalTransactionAmountSentBy() {
        assertEquals(430.2, dataFetcher.getTotalTransactionAmountSentBy("Tom Shelby"));
    }

    @Test
    void testGetMaxTransactionAmount() {
        assertEquals(430.2, dataFetcher.getMaxTransactionAmount());
    }

    @Test
    void testCountUniqueClients() {
        assertEquals(6, dataFetcher.countUniqueClients());
    }

    @Test
    void testHasOpenComplianceIssues() {
        assertTrue(dataFetcher.hasOpenComplianceIssues("Tom Shelby"));
    }

    @Test
    void testGetTransactionsByBeneficiaryName() {
        Map<String, Transaction> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();
        assertEquals(3, transactionsByBeneficiary.size());
        assertTrue(transactionsByBeneficiary.containsKey("Arthur Shelby"));
        assertTrue(transactionsByBeneficiary.containsKey("Alfie Solomons"));
        assertTrue(transactionsByBeneficiary.containsKey("Grace Burgess"));
    }

    @Test
    void testGetUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
        assertEquals(2, unsolvedIssueIds.size());
        assertTrue(unsolvedIssueIds.contains(1));
        assertTrue(unsolvedIssueIds.contains(3));
    }

    @Test
    void testGetAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
        assertEquals(1, solvedIssueMessages.size());
        assertTrue(solvedIssueMessages.containsAll(Arrays.asList("Never gonna give you up")));
    }

    @Test
    void testGetTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertEquals(430.2, top3Transactions.get(0).getAmount().doubleValue());
        assertEquals(150.2, top3Transactions.get(1).getAmount().doubleValue());
        assertEquals(150.2, top3Transactions.get(2).getAmount().doubleValue());
    }

    @Test
    void testGetTopSender() {
        Optional<String> topSender = dataFetcher.getTopSender();
        assertTrue(topSender.isPresent());
        assertEquals("Tom Shelby", topSender.get());
    }
}
