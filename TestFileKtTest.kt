import data.wrapper_classes.Result
import domain.use_cases.LaunchProgramUseCase
import domain.utils.KeyboardUtils
import domain.utils.SoftwareUtils
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class TestFileKtTest {
    @RelaxedMockK
    lateinit var softwareUtils: SoftwareUtils

    @RelaxedMockK
    lateinit var keyboardUtils: KeyboardUtils

    lateinit var launchProgramUseCase: LaunchProgramUseCase

    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err

    @Before
    fun setup() {

        MockKAnnotations.init(this)
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
        launchProgramUseCase = LaunchProgramUseCase(softwareUtils, keyboardUtils)


    }


    @Test
    fun `should print file not found when empty success result is returned`() {
        coEvery { softwareUtils.searchProgramPath(any(), any()) } returns flow { emit(Result.Success()) }
        runBlocking {
            launchProgramUseCase("test")
            assert("file not found".lowercase() in outContent.toString().lowercase())

        }
    }

    @After
    fun restoreStreams() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

}