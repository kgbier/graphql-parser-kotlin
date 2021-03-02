# graphql-parser
### Konrad Biernacki (<kgbier@gmail.com>)

[![Gradle](https://github.com/kgbier/graphql-parser-kotlin/actions/workflows/gradle.yml/badge.svg)](https://github.com/kgbier/graphql-parser-kotlin/actions/workflows/gradle.yml)

A utility for parsing GraphQL queries. Written with help from the excellent 
[Point·Free](https://www.pointfree.co/collections/parsing).

Swift version of this library: [kgbier/graphql-parser-swift](https://github.com/kgbier/graphql-parser-swift)

Current functionality is limited to understanding a GraphQL Query as detailed in the
official [spec](https://spec.graphql.org/June2018/), and producing an AST (abstract syntax tree).

### Limitations:
- Does not support Unicode literals
- Does not support block strings
