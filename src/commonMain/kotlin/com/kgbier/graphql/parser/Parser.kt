package com.kgbier.graphql.parser

import com.kgbier.graphql.parser.structure.*
import com.kgbier.graphql.parser.substring.Substring

internal interface Parser<A> {
    fun run(str: Substring): A?
}

internal fun <A> Parser<A>.parse(str: Substring) = run(str)

internal data class ParseResult<A>(val match: A?, val rest: Substring)

internal fun <A> Parser<A>.parse(str: String): ParseResult<A> {
    val substring = Substring(str)
    val match = parse(substring)
    return ParseResult(match, substring)
}

internal inline fun <A, B> Parser<A>.map(crossinline f: (A) -> B): Parser<B> = object : Parser<B> {
    override fun run(str: Substring): B? = this@map.parse(str)?.let(f)
}

internal fun <A> Parser<A>.erase(): Parser<Unit> = object : Parser<Unit> {
    override fun run(str: Substring) = this@erase.parse(str)?.let { Unit }
}

internal inline fun <A, B> Parser<A>.flatMap(crossinline f: (A) -> Parser<B>): Parser<B> = object : Parser<B> {
    override fun run(str: Substring): B? {
        val originalState = str.state
        val matchA = this@flatMap.parse(str)
        val parserB = matchA?.let(f)
        val matchB = parserB?.parse(str)
        return if (matchB == null) {
            str.state = originalState
            null
        } else matchB
    }
}

internal fun <A, B> zip(a: Parser<A>, b: Parser<B>): Parser<Tuple2<A, B>> = object : Parser<Tuple2<A, B>> {
    override fun run(str: Substring): Tuple2<A, B>? {
        val originalState = str.state
        val resultA = a.parse(str) ?: return null
        val resultB = b.parse(str)
        return if (resultB == null) {
            str.state = originalState
            null
        } else Tuple2(resultA, resultB)
    }
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
