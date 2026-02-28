package com.kwh.almuniconnect.network

fun BranchEntity.toDomain(): Branch {
    return Branch(
        id = id,
        name = name,
        shortName = shortName
    )
}

fun Branch.toEntity(): BranchEntity {
    return BranchEntity(
        id = id,
        name = name,
        shortName = shortName
    )
}