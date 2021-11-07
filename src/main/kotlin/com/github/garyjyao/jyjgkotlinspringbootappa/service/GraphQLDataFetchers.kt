package com.github.garyjyao.jyjgkotlinspringbootappa.service

import com.google.common.collect.ImmutableMap
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*

@Component
class GraphQLDataFetchers {
    private val books = Arrays.asList<Map<String, String>?>(
        ImmutableMap.of(
            "id", "book-1",
            "name", "Harry Potter and the Philosopher's Stone",
            "pageCount", "223",
            "authorId", "author-1"
        ),
        ImmutableMap.of(
            "id", "book-2",
            "name", "Moby Dick",
            "pageCount", "635",
            "authorId", "author-2"
        ),
        ImmutableMap.of(
            "id", "book-3",
            "name", "Interview with the vampire",
            "pageCount", "371",
            "authorId", "author-3"
        )
    )

    private val authors = Arrays.asList<Map<String, String>?>(
        ImmutableMap.of(
            "id", "author-1",
            "firstName", "Joanne",
            "lastName", "Rowling"
        ),
        ImmutableMap.of(
            "id", "author-2",
            "firstName", "Herman",
            "lastName", "Melville"
        ),
        ImmutableMap.of(
            "id", "author-3",
            "firstName", "Anne",
            "lastName", "Rice"
        )
    )

    fun getBookByIdDataFetcher(): DataFetcher<*>? {
        return DataFetcher<Any?> { dataFetchingEnvironment ->
            val bookId = dataFetchingEnvironment.getArgument<String>("id")
            books
                .stream()
                .filter { book: Map<String, String>? ->
                    book!!["id"] == bookId
                }
                .findFirst()
                .orElse(null)
        }
    }

    fun getAuthorDataFetcher(): DataFetcher<*>? {
        return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
            val book =
                dataFetchingEnvironment.getSource<Map<String, String>>()
            val authorId = book["authorId"]
            authors
                .stream()
                .filter { author: Map<String, String>? ->
                    author!!["id"] == authorId
                }
                .findFirst()
                .orElse(null)
        }
    }

    fun getAuthorByIdDataFetcher(): DataFetcher<*>? {
        return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
            val authorId = dataFetchingEnvironment.getArgument<String>("id")
            authors
                .stream()
                .filter { author: Map<String, String>? ->
                    author!!["id"] == authorId
                }
                .findFirst()
                .orElse(null)
        }
    }
}
