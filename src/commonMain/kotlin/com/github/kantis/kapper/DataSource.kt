package com.github.kantis.kapper

interface DataSource {
    fun transaction(block: Transaction.() -> Unit)
}

interface Transaction {
    fun execute(query: Query): List<Row>
}