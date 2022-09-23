package com.flow.android.location.ui.categories.state

import com.flow.android.location.api.model.Category

data class CategoryScreenState(
    val categories: List<Category> = emptyList(),
    val activeCategory: Category? = null
)
