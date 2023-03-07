package org.thoughtcrime.securesms.util

import android.Manifest
import android.app.Application
import androidx.documentfile.provider.DocumentFile
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.signal.core.util.logging.Log
import org.thoughtcrime.securesms.BaseUnitTest
import org.thoughtcrime.securesms.permissions.Permissions
import org.thoughtcrime.securesms.testutil.EmptyLogger
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = Application::class)
class BackupUtilTest : BaseUnitTest() {

  companion object {
    private const val TEST_NAME = "1920837192.backup"

    @BeforeClass
    @JvmStatic
    fun setUpClass() {
      Log.initialize(EmptyLogger())
    }
  }

  private val documentFile = mock(DocumentFile::class.java)
  private lateinit var permissions: MockedStatic<Permissions>
  private lateinit var storageUtil: MockedStatic<StorageUtil>
  private val directory = mock(File::class.java)

  @Before
  fun beforeEach() {
    permissions = mockStatic(Permissions::class.java)
    storageUtil = mockStatic(StorageUtil::class.java)
  }

  @After
  fun afterEach() {
    permissions.close()
    storageUtil.close()
  }

  @Test
  fun `Given a non-existent uri, when I getBackupInfoFromSingleDocumentFile, then I expect NOT_FOUND`() {
    try {
      BackupUtil.getBackupInfoFromSingleDocumentFile(documentFile)
      fail("Expected a BackupFileException")
    } catch (e: BackupUtil.BackupFileException) {
      assertEquals(BackupUtil.BackupFileState.NOT_FOUND, e.state)
    }
  }

  @Test
  fun `Given an existent but unreadable uri, when I getBackupInfoFromSingleDocumentFile, then I expect NOT_READABLE`() {
    givenFileExists()

    try {
      BackupUtil.getBackupInfoFromSingleDocumentFile(documentFile)
      fail("Expected a BackupFileException")
    } catch (e: BackupUtil.BackupFileException) {
      assertEquals(BackupUtil.BackupFileState.NOT_READABLE, e.state)
    }
  }

  @Test
  fun `Given an existent readable uri with a bad extension, when I getBackupInfoFromSingleDocumentFile, then I expect UNSUPPORTED_FILE_EXTENSION`() {
    givenFileExists()
    givenFileIsReadable()

    try {
      BackupUtil.getBackupInfoFromSingleDocumentFile(documentFile)
      fail("Expected a BackupFileException")
    } catch (e: BackupUtil.BackupFileException) {
      assertEquals(BackupUtil.BackupFileState.UNSUPPORTED_FILE_EXTENSION, e.state)
    }
  }

  @Test
  fun `Given an existent readable uri, when I getBackupInfoFromSingleDocumentFile, then I expect an info`() {
    givenFileExists()
    givenFileIsReadable()
    givenFileHasCorrectExtension()

    val info = BackupUtil.getBackupInfoFromSingleDocumentFile(documentFile)
    assertNotNull(info)
  }

  private fun givenFileExists() {
    doReturn(true).`when`(documentFile).exists()
  }

  private fun givenFileIsReadable() {
    doReturn(true).`when`(documentFile).canRead()
  }

  private fun givenFileHasCorrectExtension() {
    doReturn(TEST_NAME).`when`(documentFile).name
  }

  @Test
  fun `Given required permission and the directory has files inside, when I hasBackupFiles, then I expect true`() {
    permissions.`when`<Boolean> { Permissions.hasAll(context, Manifest.permission.READ_EXTERNAL_STORAGE) }.thenReturn(true)
    storageUtil.`when`<File> { StorageUtil.getBackupDirectory() }.thenReturn(directory)
    doReturn(true).`when`(directory).exists()
    doReturn(true).`when`(directory).isDirectory
    doReturn(arrayOf(File("file1"))).`when`(directory).listFiles()
    assertTrue(BackupUtil.hasBackupFiles(context))
  }

  @Test
  fun `Given no permission and the directory has files inside, when I hasBackupFiles, then I expect false`() {
    permissions.`when`<Boolean> { Permissions.hasAll(context, Manifest.permission.READ_EXTERNAL_STORAGE) }.thenReturn(false)
    storageUtil.`when`<File> { StorageUtil.getBackupDirectory() }.thenReturn(directory)
    doReturn(true).`when`(directory).exists()
    doReturn(true).`when`(directory).isDirectory
    doReturn(arrayOf(File("file1"))).`when`(directory).listFiles()
    assertFalse(BackupUtil.hasBackupFiles(context))
  }

  @Test
  fun `Given required permission and the directory doesn't exist, when I hasBackupFiles, then I expect false`() {
    permissions.`when`<Boolean> { Permissions.hasAll(context, Manifest.permission.READ_EXTERNAL_STORAGE) }.thenReturn(true)
    storageUtil.`when`<File> { StorageUtil.getBackupDirectory() }.thenReturn(directory)
    doReturn(false).`when`(directory).exists()
    doReturn(false).`when`(directory).isDirectory
    doReturn(arrayOf(File("file1"))).`when`(directory).listFiles()
    assertFalse(BackupUtil.hasBackupFiles(context))
  }

  @Test
  fun `Given required permission and the file exists but it's not a directory, when I hasBackupFiles, then I expect false`() {
    permissions.`when`<Boolean> { Permissions.hasAll(context, Manifest.permission.READ_EXTERNAL_STORAGE) }.thenReturn(true)
    storageUtil.`when`<File> { StorageUtil.getBackupDirectory() }.thenReturn(directory)
    doReturn(true).`when`(directory).exists()
    doReturn(false).`when`(directory).isDirectory
    doReturn(arrayOf(File("file1"))).`when`(directory).listFiles()
    assertFalse(BackupUtil.hasBackupFiles(context))
  }

  @Test
  fun `Given required permission and the directory exists but it doesn't have files inside, when I hasBackupFiles, then I expect false`() {
    permissions.`when`<Boolean> { Permissions.hasAll(context, Manifest.permission.READ_EXTERNAL_STORAGE) }.thenReturn(true)
    storageUtil.`when`<File> { StorageUtil.getBackupDirectory() }.thenReturn(directory)
    doReturn(true).`when`(directory).exists()
    doReturn(true).`when`(directory).isDirectory
    doReturn(null).`when`(directory).listFiles()
    assertFalse(BackupUtil.hasBackupFiles(context))
    doReturn(emptyArray<File>()).`when`(directory).listFiles()
    assertFalse(BackupUtil.hasBackupFiles(context))
  }
}
