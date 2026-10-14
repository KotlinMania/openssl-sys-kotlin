// port-lint: source handwritten/ec.rs
package io.github.kotlinmania.opensslsys

public enum class PointConversionFormT(
    public val value: Int,
) {
    POINT_CONVERSION_COMPRESSED(2),
    POINT_CONVERSION_UNCOMPRESSED(4),
    POINT_CONVERSION_HYBRID(6),
}

public class EcMethod(
    public val address: Long = 0L,
)

public class EcGroup(
    public val address: Long = 0L,
)

public class EcPoint(
    public val address: Long = 0L,
)

public class EcdsaSig(
    public val address: Long = 0L,
)
