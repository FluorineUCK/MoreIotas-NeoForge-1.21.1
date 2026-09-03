package ram.talia.moreiotas.api

import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import com.mojang.datafixers.util.Either
import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import org.ejml.simple.SimpleMatrix
import org.ejml.simple.SimpleOperations
import ram.talia.moreiotas.api.casting.iota.EntityTypeIota
import ram.talia.moreiotas.api.casting.iota.IotaTypeIota
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota
import ram.talia.moreiotas.api.casting.iota.MatrixIota
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.api.util.Anyone

operator fun Double.times(vec: Vec3): Vec3 = vec.scale(this)
operator fun Vec3.times(d: Double): Vec3 = scale(d)
operator fun Vec3.div(d: Double): Vec3 = scale(1.0 / d)
operator fun Vec3.plus(vec3: Vec3): Vec3 = add(vec3)
operator fun Vec3.minus(vec3: Vec3): Vec3 = subtract(vec3)
operator fun Vec3.unaryMinus(): Vec3 = scale(-1.0)

operator fun Position.component1(): Double = x()
operator fun Position.component2(): Double = y()
operator fun Position.component3(): Double = z()

operator fun Double.times(mat: SimpleMatrix): SimpleMatrix =
    mat.elementOp(SimpleOperations.ElementOpReal { _, _, value -> value * this })

operator fun SimpleMatrix.times(d: Double): SimpleMatrix =
    elementOp(SimpleOperations.ElementOpReal { _, _, value -> value * d })

operator fun Vec3.times(mat: SimpleMatrix): SimpleMatrix = mat.mult(asMatrix)
operator fun SimpleMatrix.times(vec3: Vec3): SimpleMatrix = mult(vec3.asMatrix)
operator fun SimpleMatrix.times(mat: SimpleMatrix): SimpleMatrix = mult(mat)
operator fun SimpleMatrix.unaryMinus(): SimpleMatrix = this * -1.0

fun List<Iota>.getBoolOrNull(idx: Int, argc: Int = 0): Boolean? {
    val value = getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, size) }
    return when (value) {
        is BooleanIota -> value.bool
        is NullIota -> null
        else -> throw MishapInvalidIota.ofType(value, reverseIndex(idx, argc), "booleannull")
    }
}

fun List<Iota>.getString(idx: Int, argc: Int = 0): String {
    val value = getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, size) }
    return (value as? StringIota)?.string
        ?: throw MishapInvalidIota.ofType(value, reverseIndex(idx, argc), "string")
}

fun List<Iota>.getStringOrNull(idx: Int, argc: Int = 0): String? {
    val value = getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, size) }
    return when (value) {
        is StringIota -> value.string
        is NullIota -> null
        else -> throw MishapInvalidIota.ofType(value, reverseIndex(idx, argc), "string")
    }
}

fun List<Iota>.getStringOrList(idx: Int, argc: Int = 0): Either<String, List<String>> {
    val value = getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, size) }
    return when (value) {
        is StringIota -> Either.left(value.string)
        is ListIota -> {
            val strings = value.list.map {
                (it as? StringIota)?.string
                    ?: throw MishapInvalidIota.ofType(value, reverseIndex(idx, argc), "stringstringlist")
            }
            Either.right(strings)
        }
        else -> throw MishapInvalidIota.ofType(value, reverseIndex(idx, argc), "stringstringlist")
    }
}

fun List<Iota>.getMatrix(idx: Int, argc: Int = 0): SimpleMatrix {
    val value = getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, size) }
    return (value as? MatrixIota)?.matrix
        ?: throw MishapInvalidIota.ofType(value, reverseIndex(idx, argc), "matrix")
}

fun List<Iota>.getNumOrVecOrMatrix(idx: Int, argc: Int = 0): Anyone<Double, Vec3, SimpleMatrix> {
    val value = getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, size) }
    return when (value) {
        is DoubleIota -> Anyone.first(value.double)
        is Vec3Iota -> Anyone.second(value.vec3)
        is MatrixIota -> Anyone.third(value.matrix)
        else -> throw MishapInvalidIota.of(value, reverseIndex(idx, argc), "numvecmat")
    }
}

fun List<Iota>.getEntityType(idx: Int, argc: Int = 0, level: ServerLevel): EntityType<*> {
    val value = getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, size) }
    return when (value) {
        is EntityTypeIota -> value.entityType
        is EntityIota -> value.getEntity(level).type
        else -> throw MishapInvalidIota.ofType(value, reverseIndex(idx, argc), "type.entity")
    }
}

fun List<Iota>.getItemType(idx: Int, argc: Int = 0): Item {
    val value = getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, size) }
    return (value as? ItemTypeIota)?.item
        ?: throw MishapInvalidIota.ofType(value, reverseIndex(idx, argc), "type.item")
}

fun List<Iota>.getItemStack(index: Int, argc: Int = 0): ItemStack {
    val value = getOrElse(index) { throw MishapNotEnoughArgs(index + 1, size) }
    return (value as? ItemStackIota)?.itemStack
        ?: throw MishapInvalidIota.of(value, reverseIndex(index, argc), "item_stack")
}

inline val String.asActionResult get() = listOf(StringIota.make(this))
inline val SimpleMatrix.asActionResult get() = listOf(MatrixIota(this))
inline val IotaType<*>.asActionResult get() = listOf(IotaTypeIota(this))
inline val Block.asActionResult get() = listOf(ItemTypeIota(this))
inline val EntityType<*>.asActionResult get() = listOf(EntityTypeIota(this))
inline val Item.asActionResult get() = listOf(ItemTypeIota(this))
inline val List<Item>.asActionResult get() = listOf(ListIota(map(::ItemTypeIota)))
inline val ItemStack.asActionResult get() = listOf(ItemStackIota.createFiltered(this))

inline val Vec3.asMatrix get() = SimpleMatrix(3, 1, false, x, y, z)
inline val BlockPos.asMatrix
    get() = SimpleMatrix(1, 3, false, x.toDouble(), y.toDouble(), z.toDouble())

inline val List<Vec3>.asMatrix
    get(): SimpleMatrix {
        val matrix = SimpleMatrix(size, 3)
        forEachIndexed { row, vec ->
            matrix.set(row, 0, vec.x)
            matrix.set(row, 1, vec.y)
            matrix.set(row, 2, vec.z)
        }
        return matrix
    }

inline val Anyone<Double, Vec3, SimpleMatrix>.asMatrix
    get() = flatMap(
        { value -> SimpleMatrix(1, 1, false, value) },
        { vec -> vec.asMatrix },
        { matrix -> matrix },
    )

inline val SimpleMatrix.asVec3 get() = Vec3(get(0), get(1), get(2))

fun MishapInvalidIota.Companion.matrixWrongSize(
    perpetrator: Iota,
    reverseIdx: Int,
    expectedRows: Int?,
    expectedColumns: Int?,
): MishapInvalidIota {
    require(expectedRows != null || expectedColumns != null) {
        "Need at least one of expectedRows and expectedColumns non-null."
    }
    return when {
        expectedRows == null -> of(perpetrator, reverseIdx, "matrix.wrong_size", "n", expectedColumns!!)
        expectedColumns == null -> of(perpetrator, reverseIdx, "matrix.wrong_size", expectedRows, "n")
        else -> of(perpetrator, reverseIdx, "matrix.wrong_size", expectedRows, expectedColumns)
    }
}

private fun reverseIndex(index: Int, argc: Int): Int =
    if (argc == 0) index else argc - (index + 1)
