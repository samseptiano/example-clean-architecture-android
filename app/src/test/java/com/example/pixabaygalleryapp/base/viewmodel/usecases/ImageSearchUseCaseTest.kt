package com.example.pixabaygalleryapp.base.viewmodel.usecases

import com.example.pixabaygalleryapp.MockTestUtil
import com.example.pixabaygalleryapp.data.repository.image.ImageRepository
import com.example.pixabaygalleryapp.domain.image.ImageSearchUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author SamuelSep on 4/21/2021.
 */
@RunWith(JUnit4::class)
class ImageSearchUseCaseTest {

    @MockK
    private lateinit var repository: ImageRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test getSearchImages gives list of images`() = runBlocking {
        //Given
        val usecase = ImageSearchUseCase(repository)
        val mockImages = MockTestUtil.createImages()

        //When
        coEvery { repository.getSearchImages(any(),any()) }
            .returns(flowOf(mockImages))

        //Invoke
        val imageListFlow = usecase(1,"")

        //Then
        MatcherAssert.assertThat(imageListFlow, CoreMatchers.notNullValue())

        val imageListDataState = imageListFlow.first()
        MatcherAssert.assertThat(imageListDataState, CoreMatchers.notNullValue())

        MatcherAssert.assertThat(imageListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            imageListDataState.imagesInfo.size,
            CoreMatchers.`is`(mockImages.imagesInfo.size)
        )


    }



    @After
    fun tearDown() {
    }
}