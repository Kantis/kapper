import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kapper.conventions.KapperBuildLogicSettings

class IsReleaseVersionTest : FreeSpec (
   {
      "isReleaseVersion" - {
         fun isReleaseVersion(version: String): Boolean = version.matches(KapperBuildLogicSettings.releaseVersionRegex)

         "should return true for a release version" {
            isReleaseVersion("1.2.3") shouldBe true
         }
         "should return false for a snapshot version" {
            isReleaseVersion("1.2.3-SNAPSHOT") shouldBe false
         }
         "should return false for a milestone version" {
            isReleaseVersion("1.2.3-M1") shouldBe false
         }
         "should return false for a release candidate version" {
            isReleaseVersion("1.2.3-RC1") shouldBe false
         }
         "should return false for a pre-release version" {
            isReleaseVersion("1.2.3-alpha1") shouldBe false
         }
      }
   }
)
