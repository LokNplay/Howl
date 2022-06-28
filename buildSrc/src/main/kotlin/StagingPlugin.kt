import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class StagingPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		val androidComponents = project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
		androidComponents.finalizeDsl { extension ->
			extension.buildTypes.create("staging").let {
				it.initWith(extension.buildTypes.getByName("debug"))
				it.isMinifyEnabled = true
				it.isShrinkResources = true
				it.proguardFiles("proguard-android-optimize.txt")
				it.applicationIdSuffix = ".staging"
			}
		}
	}
}

class ModuleStagingPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
		androidComponents.finalizeDsl { extension ->
			extension.buildTypes.create("staging").let {
				it.initWith(extension.buildTypes.getByName("debug"))
				it.isMinifyEnabled = true
				it.proguardFiles("proguard-android-optimize.txt")
			}
		}
	}
}