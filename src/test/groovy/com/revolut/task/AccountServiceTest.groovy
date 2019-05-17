/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
package com.revolut.task

import com.revolut.task.exception.BalanceNegativeException
import com.revolut.task.exception.NegativeTransferAmountException
import spock.lang.Specification
import com.revolut.task.model.Account

import com.revolut.task.exception.AccountNotFoundException

class AccountServiceTest extends Specification {

    AccountService service;  

    def setup() {
        service = new AccountService();
    }

    def "creates new account"() {
        when:
        def result = service.createAccount()

        then:
        result != null
        result instanceof Account
        result.id != null
    }

    def "returns an account"() {
        given:
        def id = service.createAccount().getId()

        when:
        def result = service.getAccount(id)

        then:
        result != null
        result instanceof Account
        result.getId() == id
    }

    def "throws an exception if account not found"() {
        when:
        service.getAccount("1")

        then:
        thrown AccountNotFoundException
    }

    def "deletes an account"() {
        given:
        def id = service.createAccount().getId();

        when:
        def result = service.deleteAccount(id)
        service.getAccount(result.getId())

        then:
        result != null
        result instanceof Account
        thrown AccountNotFoundException
    }

    def "updates an account balance"() {
        given:
        def id = service.createAccount().getId();

        when:
        def result = service.updateAccount(id, new BigDecimal(1))

        then:
        result != null
        result instanceof Account
        result.balance == new BigDecimal(1)
    }

    def "transfers money from one account to another"() {
        given:
        def acc1 = service.createAccount().getId()
        def acc2 = service.createAccount().getId()
        service.updateAccount(acc1, new BigDecimal(10))

        when:
        service.transferMoney(acc1, acc2, new BigDecimal(10))

        then:
        def updatedAcc = service.getAccount(acc2)
        updatedAcc.getBalance() == 10

    }

    def "throws exception when it would result in negative balance"() {
        given:
        def acc1 = service.createAccount().getId()
        def acc2 = service.createAccount().getId()

        when:
        service.transferMoney(acc1, acc2, new BigDecimal(10))

        then:
        thrown BalanceNegativeException

    }

    def "thrown exception if negative amount is transfered"() {
        when:
        service.transferMoney("1", "2", new BigDecimal(-1))

        then:
        thrown NegativeTransferAmountException
    }

}
