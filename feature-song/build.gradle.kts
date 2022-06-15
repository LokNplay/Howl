plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

android {
	compileSdk = Android.compileSdk

	defaultConfig {
		minSdk = Android.minSdk
		targetSdk = Android.compileSdk

		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
		buildConfig = false
		aidl = false
		renderScript = false
		resValues = false
		shaders = false
	}
	composeOptions {
		kotlinCompilerExtensionVersion = Compose.composeCompiler
	}
}

dependencies {
	implementation(project(Modules.coreDatabase))
	implementation(project(Modules.coreModel))
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreData))
	implementation(project(Modules.coreNavigation))
	implementation(project(Modules.coreUi))
	implementation(project(Modules.coreService))
	implementation(project(Modules.featurePlayer))

	implementation(Lifecycle.lifecycleViewModelCompose)

	implementation(ExoPlayer.exoplayerMediaSession)

	implementation(Hilt.android)
	kapt(Hilt.compiler)
}