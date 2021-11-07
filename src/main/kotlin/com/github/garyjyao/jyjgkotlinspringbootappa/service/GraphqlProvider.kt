package com.github.garyjyao.jyjgkotlinspringbootappa.service

import com.google.common.base.Charsets
import com.google.common.io.Resources
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.io.IOException
import javax.annotation.PostConstruct

@Component
class GraphqlProvider {
    private var graphQL: GraphQL? = null

    @Bean
    fun graphQL(): GraphQL? {
        return graphQL
    }

    @PostConstruct
    @Throws(IOException::class)
    fun init() {
        val url = Resources.getResource("schema.graphqls")
        val sdl = Resources.toString(url, Charsets.UTF_8)
        val graphQLSchema = buildSchema(sdl)
        graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    }

    @Autowired
    var graphQLDataFetchers: GraphQLDataFetchers? = null

    private fun buildSchema(sdl: String): GraphQLSchema {
        val typeRegistry = SchemaParser().parse(sdl)
        val runtimeWiring = buildWiring()
        val schemaGenerator = SchemaGenerator()
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
    }

    private fun buildWiring(): RuntimeWiring {
        return RuntimeWiring.newRuntimeWiring()
            .type(
                TypeRuntimeWiring.newTypeWiring("Query")
                    .dataFetcher("bookById", graphQLDataFetchers!!.getBookByIdDataFetcher())
            )
            .type(
                TypeRuntimeWiring.newTypeWiring("Book")
                    .dataFetcher("author", graphQLDataFetchers!!.getAuthorDataFetcher())
            )
            .type(
                TypeRuntimeWiring.newTypeWiring("Query")
                    .dataFetcher("authorById", graphQLDataFetchers!!.getAuthorByIdDataFetcher())
            )
            .build()
    }
}
