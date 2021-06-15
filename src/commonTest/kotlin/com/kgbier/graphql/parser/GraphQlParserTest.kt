package com.kgbier.graphql.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class GraphQlParserTest {

    // These queries are taken from https://graphql.org/learn/queries/ as reasonable examples to handle

    private fun testQuery(query: String) {
        val result = GraphQLParser.parseWithResult(query)
        assertNotNull(result.match)
        assertEquals("", result.rest.toString())
    }

    @Test
    fun fields() {
        val query = """
        {
          hero {
            name
            friends {
              name
            }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun comments() {
        val query = """
        {
          hero {
            name
            # Queries can have comments!
            friends {
              name
            }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun commentsInline() {
        val query = """
        {
          hero {
            name # Comments run from the start of the comment to the end of the line
            # friends {
            #   name
            # }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun arguments() {
        val query = """
        {
          human(id: "1000") {
            name
            height
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun argumentsField() {
        val query = """
        {
          human(id: "1000") {
            name
            height(unit: FOOT)
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun aliases() {
        val query = """
        {
          empireHero: hero(episode: EMPIRE) {
            name
          }
          jediHero: hero(episode: JEDI) {
            name
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun fragments() {
        val query = """
        {
          leftComparison: hero(episode: EMPIRE) {
            ...comparisonFields
          }
          rightComparison: hero(episode: JEDI) {
            ...comparisonFields
          }
        }

        fragment comparisonFields on Character {
          name
          appearsIn
          friends {
            name
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun fragmentsWithVariables() {
        val query = """
        query HeroComparison(${"$"}first: Int = 3) {
          leftComparison: hero(episode: EMPIRE) {
            ...comparisonFields
          }
          rightComparison: hero(episode: JEDI) {
            ...comparisonFields
          }
        }

        fragment comparisonFields on Character {
          name
          friendsConnection(first: ${"$"}first) {
            totalCount
            edges {
              node {
                name
              }
            }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun operationName() {
        val query = """
        query HeroNameAndFriends {
          hero {
            name
            friends {
              name
            }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun variables() {
        val query = """
        query HeroNameAndFriends(${"$"}episode: Episode) {
          hero(episode: ${"$"}episode) {
            name
            friends {
              name
            }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun variablesDefault() {
        val query = """
        query HeroNameAndFriends(${"$"}episode: Episode = JEDI) {
          hero(episode: ${"$"}episode) {
            name
            friends {
              name
            }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun directives() {
        val query = """
        query Hero(${"$"}episode: Episode, ${"$"}withFriends: Boolean!) {
          hero(episode: ${"$"}episode) {
            name
            friends @include(if: ${"$"}withFriends) {
              name
            }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun mutations() {
        val query = """
        mutation CreateReviewForEpisode(${"$"}ep: Episode!, ${"$"}review: ReviewInput!) {
          createReview(episode: ${"$"}ep, review: ${"$"}review) {
            stars
            commentary
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun inlineFragment() {
        val query = """
        query HeroForEpisode(${"$"}ep: Episode!) {
          hero(episode: ${"$"}ep) {
            name
            ... on Droid {
              primaryFunction
            }
            ... on Human {
              height
            }
          }
        }
        """
        testQuery(query)
    }

    @Test
    fun metaFields() {
        val query = """
        {
          search(text: "an") {
            __typename
            ... on Human {
              name
            }
            ... on Droid {
              name
            }
            ... on Starship {
              name
            }
          }
        }
        """
        testQuery(query)
    }
}
