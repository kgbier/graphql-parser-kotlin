package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.structure.*
import com.kgbier.graphql.parser.substring.Substring

internal fun interface Parser<Output> {
    operator fun invoke(str: Substring): Output?
}

internal data class ParseResult<Output>(val match: Output?, val remainder: Substring)

internal fun <A> Parser<A>.parse(str: String): ParseResult<A> {
    val substring = Substring(str)
    val match = invoke(substring)
    return ParseResult(match, substring)
}

internal inline fun <A, B> Parser<A>.map(
    crossinline transform: (A) -> B,
): Parser<B> = Parser { invoke(it)?.let(transform) }

internal fun <A> Parser<A>.erase(): Parser<Unit> =
    Parser { invoke(it)?.let { } }

internal inline fun <A, B> Parser<A>.flatMap(
    crossinline f: (A) -> Parser<B>,
): Parser<B> = Parser {
    val originalState = it.state
    val matchA = invoke(it)
    val parserB = matchA?.let(f)
    val matchB = parserB?.invoke(it)
    if (matchB == null) {
        it.state = originalState
        null
    } else matchB
}

internal fun <A, B> zip(a: Parser<A>, b: Parser<B>): Parser<Tuple2<A, B>> = Parser {
    val originalState = it.state
    val resultA = a(it) ?: return@Parser null
    val resultB = b(it)
    if (resultB == null) {
        it.state = originalState
        null
    } else Tuple2(resultA, resultB)
}

internal fun <A, B, C> zip(
    a: Parser<A>,
    b: Parser<B>,
    c: Parser<C>
): Parser<Tuple3<A, B, C>> = zip(a, zip(b, c))
    .map { (a, bc) ->
        Tuple3(a, bc.first, bc.second)
    }

internal fun <A, B, C, D> zip(
    a: Parser<A>,
    b: Parser<B>,
    c: Parser<C>,
    d: Parser<D>
): Parser<Tuple4<A, B, C, D>> = zip(a, zip(b, c, d))
    .map { (a, bcd) ->
        Tuple4(a, bcd.first, bcd.second, bcd.third)
    }

internal fun <A, B, C, D, E> zip(
    a: Parser<A>,
    b: Parser<B>,
    c: Parser<C>,
    d: Parser<D>,
    e: Parser<E>
): Parser<Tuple5<A, B, C, D, E>> = zip(a, zip(b, c, d, e))
    .map { (a, bcde) ->
        Tuple5(a, bcde.first, bcde.second, bcde.third, bcde.fourth)
    }

internal fun <A, B, C, D, E, F> zip(
    a: Parser<A>,
    b: Parser<B>,
    c: Parser<C>,
    d: Parser<D>,
    e: Parser<E>,
    f: Parser<F>
): Parser<Tuple6<A, B, C, D, E, F>> = zip(a, zip(b, c, d, e, f))
    .map { (a, bcdef) ->
        Tuple6(a, bcdef.first, bcdef.second, bcdef.third, bcdef.fourth, bcdef.fifth)
    }

internal fun <A, B, C, D, E, F, G> zip(
    a: Parser<A>,
    b: Parser<B>,
    c: Parser<C>,
    d: Parser<D>,
    e: Parser<E>,
    f: Parser<F>,
    g: Parser<G>
): Parser<Tuple7<A, B, C, D, E, F, G>> = zip(a, zip(b, c, d, e, f, g))
    .map { (a, bcdefg) ->
        Tuple7(a, bcdefg.first, bcdefg.second, bcdefg.third, bcdefg.fourth, bcdefg.fifth, bcdefg.sixth)
    }

internal fun <A, B, C, D, E, F, G, H> zip(
    a: Parser<A>,
    b: Parser<B>,
    c: Parser<C>,
    d: Parser<D>,
    e: Parser<E>,
    f: Parser<F>,
    g: Parser<G>,
    h: Parser<H>
): Parser<Tuple8<A, B, C, D, E, F, G, H>> = zip(a, zip(b, c, d, e, f, g, h))
    .map { (a, bcdefgh) ->
        Tuple8(a, bcdefgh.first, bcdefgh.second, bcdefgh.third, bcdefgh.fourth, bcdefgh.fifth, bcdefgh.sixth, bcdefgh.seventh)
    }

internal fun <A, B, C, D, E, F, G, H, I> zip(
    a: Parser<A>,
    b: Parser<B>,
    c: Parser<C>,
    d: Parser<D>,
    e: Parser<E>,
    f: Parser<F>,
    g: Parser<G>,
    h: Parser<H>,
    i: Parser<I>
): Parser<Tuple9<A, B, C, D, E, F, G, H, I>> = zip(a, zip(b, c, d, e, f, g, h, i))
    .map { (a, bcdefghi) ->
        Tuple9(a, bcdefghi.first, bcdefghi.second, bcdefghi.third, bcdefghi.fourth, bcdefghi.fifth, bcdefghi.sixth, bcdefghi.seventh, bcdefghi.eighth)
    }
