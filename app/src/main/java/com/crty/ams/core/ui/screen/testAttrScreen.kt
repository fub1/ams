package com.crty.ams.core.ui.screen

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.ui.viewmodel.AttributeViewModel

// Sample data for preview (replace with your actual data)
val sampleAttributes = listOf(
    AttributeEntity.Organization(1,0,"ddd"),
    AttributeEntity.Organization(2,0,"ddd2"),
    AttributeEntity.Organization(3,0,"ddd3"),
    AttributeEntity.Organization(4,1,"ddd4"),
    AttributeEntity.Organization(5,1,"ddd5"),
    AttributeEntity.Organization(6,2,"ddd6"),
    AttributeEntity.Organization(7,2,"ddd7"),
    AttributeEntity.Organization(8,3,"ddd8"),
    AttributeEntity.Organization(9,3,"ddd9"),
    AttributeEntity.Organization(10,3,"ddd10"),
    AttributeEntity.Organization(11,4,"ddd11"),
    AttributeEntity.Organization(12,4,"ddd12"),
    AttributeEntity.Organization(13,5,"ddd13"),

)

@Preview(showBackground = true)
@Composable
fun AttributeBottomSheetPreview() {
    val uiState = remember {
        AttributeViewModel.UIState(
            attributes = sampleAttributes,
            firstLevelOptions = sampleAttributes.filter { it.parentId == 0 },
            firstLevelSelectedId = sampleAttributes.firstOrNull()?.id,
            firstLevelString = sampleAttributes.firstOrNull()?.name ?: "",
            grandAddOrgPermission = true,

            // ... other properties based on your UIState
        )
    }

    AttributeBottomSheet(
        attributeName = "Organization", // Example attribute name
        onDismiss = {}
    )
}