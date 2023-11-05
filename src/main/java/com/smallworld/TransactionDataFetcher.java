package com.smallworld;

import com.smallworld.data.Transaction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionDataFetcher {

    private final List<Transaction> transactionList;

    public TransactionDataFetcher(List<Transaction> transactionList ){
        this.transactionList = transactionList;
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        return transactionList
                .stream()
                .mapToDouble(transaction ->  transaction.getAmount().doubleValue()) // map to transaction amount value
                .sum(); // sum all the amount values
//        throw new UnsupportedOperationException();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return transactionList
                .stream()
                .filter(t -> t.getSenderFullName().equals(senderFullName)) // filter transactions based on sender's full name
                .mapToDouble(transaction ->  transaction.getAmount().doubleValue()) // map to transaction amount value
                .sum(); // sum all the transaction amount values
//        throw new UnsupportedOperationException();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        return transactionList
                .stream()
                .mapToDouble(transaction -> transaction.getAmount().doubleValue()).max().getAsDouble(); // first map to transaction amount then get the max value as double
//        throw new UnsupportedOperationException();
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        return transactionList.stream()
                .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName())) // map to sender and beneficiary both
                .distinct() // get the unique only
                .count(); // then get the count
//        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        Optional<Transaction> transactionOptional = transactionList
                .stream()
                .filter(
                        transaction -> (
                                transaction.getBeneficiaryFullName().equals(clientFullName) ||
                                        transaction.getSenderFullName().equals(clientFullName)) &&
                                transaction.getIssueSolved() == Boolean.FALSE ).findAny();
        return transactionOptional.isPresent() ? Boolean.TRUE : Boolean.FALSE;
//        throw new UnsupportedOperationException();
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        Map<String, Transaction> map;
        try {
            map = transactionList.stream()
                    .collect(Collectors.toMap(Transaction::getBeneficiaryFullName, Transaction::new)); // map to every transaction against the beneficiary name
        }
        catch (IllegalStateException exception) {
            throw new UnsupportedOperationException(exception.getMessage()); // failed to map if beneficiaries are not unique
        }
        return map;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        return transactionList
                .stream()
                .filter(transaction -> transaction.getIssueSolved() == Boolean.FALSE) // filter the transactions which are still open
                .map(Transaction::getIssueId) // map to issue id
                .collect(Collectors.toSet()); // convert it to set
//        throw new UnsupportedOperationException();
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return transactionList
                .stream()
                .filter(t -> t.getIssueSolved() == Boolean.TRUE) // filter the transactions which are solved
                .map(Transaction::getIssueMessage) // map to issue message
                .collect(Collectors.toList()); // convert to list
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactionList
                .stream()
                .sorted((t1, t2) -> Double.compare(t2.getAmount().doubleValue(), t1.getAmount().doubleValue())) // sort transactions in ascending order
                .limit(3) // get top 3
                .collect(Collectors.toList()); // convert to list
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        return Optional
                .of(
                        transactionList
                                .stream()
                                .collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(transaction ->  transaction.getAmount().doubleValue()))) // map to sender name against the sum of all it's transactions
                                .entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey() // get the max by value (transaction amount) then get the key (sender name)
                );
//        throw new UnsupportedOperationException();
    }

}
