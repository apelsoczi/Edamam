package com.pelsoczi.edamam.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.pelsoczi.edamam.api.RecipesResponse
import com.pelsoczi.edamam.repository.RecipeRepository
import com.pelsoczi.edamam.util.CoroutinesTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class RecipesViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: RecipesViewModel

    @MockK
    lateinit var repository: RecipeRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = mockk()
        viewModel = RecipesViewModel(
            repository,
            coroutinesTestRule.testDispatcher
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun search_error_query() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val emissions = mutableListOf<RecipesViewState>()
        viewModel.viewState.asLiveData().observeForever {
            emissions.add(it)
        }

        viewModel.searchRepository(" ")

        assert(emissions.size == 2)
        assert(emissions[0] is RecipesViewState.Empty)
        assert(emissions[1] is RecipesViewState.Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun search_success() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val emissions = mutableListOf<RecipesViewState>()
        viewModel.viewState.asLiveData().observeForever {
            emissions.add(it)
        }
        coEvery { repository.search(any()) } returns RecipesResponse.RecipesSuccess(emptyList())

        viewModel.searchRepository("paprika")

        assert(emissions.size == 3)
        assert(emissions[0] is RecipesViewState.Empty)
        assert(emissions[1] is RecipesViewState.Loading)
        assert(emissions[2] is RecipesViewState.Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun reset() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val emissions = mutableListOf<RecipesViewState>()
        viewModel.viewState.asLiveData().observeForever {
            emissions.add(it)
        }
        coEvery { repository.search(any()) } returns RecipesResponse.RecipesSuccess(emptyList())
        viewModel.searchRepository("paprika")

        viewModel.reset()

        assert(emissions.last() is RecipesViewState.Empty)
    }

}