package com.revolut.task

import com.revolut.task.exception.AccountNotFoundException
import com.revolut.task.model.Account
import spock.lang.Specification

class AccountRepositoryTest extends Specification {

    AccountRepository repository

    def setup() {
        repository = new AccountRepository()
    }

    def "creates new account"() {
        when:
        def result = repository.createAccount()

        then:
        result != null
        result instanceof Account
        result.id != null
    }

    def "returns an account"() {
        given:
        def id = repository.createAccount().getId()

        when:
        def result = repository.getAccount(id)

        then:
        result != null
        result instanceof Account
        result.getId() == id
    }

    def "throws an exception if account not found"() {
        when:
        repository.getAccount("1")

        then:
        thrown AccountNotFoundException
    }

    def "deletes an account"() {
        given:
        def id = repository.createAccount().getId()

        when:
        def result = repository.deleteAccount(id)
        repository.getAccount(id)

        then:
        result != null
        result instanceof Account
        thrown AccountNotFoundException

    }

    def "updates an account balance"() {
        given:
        def id = repository.createAccount().getId()

        when:
        def result = repository.updateAccount(id, new BigDecimal(1))

        then:
        result != null
        result instanceof Account
        result.balance == new BigDecimal(1)
    }
}
