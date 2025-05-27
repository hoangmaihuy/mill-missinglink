package io.github.hoangmaihuy.missinglink

import mill.testkit.IntegrationTester
import utest.*

import scala.runtime.stdLibPatches.Predef.assert

object IntegrationTests extends TestSuite {

  val resourceFolder = os.Path(sys.env("MILL_TEST_RESOURCE_DIR"))

  def tests: Tests = Tests {
    println("initializing mill-missinglink.IntegrationTest.tests")

    def missinglinkTest(module: String)(isSuccess: Boolean) = {
      val tester = new IntegrationTester(
        daemonMode = true,
        workspaceSourcePath = resourceFolder / "integration" / module,
        millExecutable = os.Path(sys.env("MILL_EXECUTABLE_PATH"))
      )
      val res = tester.eval("example.missinglinkCheck")
      println(res.out)
      println(res.err)
      assert(res.isSuccess == isSuccess)
    }

    missinglinkTest("do-not-fail-on-conflicts")(isSuccess = true)
    missinglinkTest("exclude-problematic-dependency")(isSuccess = false)
    missinglinkTest("has-problematic-dependency")(isSuccess = false)
    missinglinkTest("ignore-destination-package")(isSuccess = true)
    missinglinkTest("ignore-source-package")(isSuccess = true)
    missinglinkTest("ignores-unused-dependency")(isSuccess = true)
    missinglinkTest("scans-dependencies")(isSuccess = false)
    missinglinkTest("target-destination-package")(isSuccess = true)
    missinglinkTest("target-source-package")(isSuccess = false)
    missinglinkTest("uses-problematic-dependency")(isSuccess = false)
  }

}
