apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
	implementation(project(Modules.components))
	implementation(project(Modules.uiSongs))
	implementation(project(Modules.coreModel))

	implementation(Coil.coil)
	implementation(Compose.runtime)
	implementation(Lifecycle.lifecycleViewModelCompose)
}