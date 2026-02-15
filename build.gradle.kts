plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "oj.nuoccam.disableenchant"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    // Sử dụng Paper API thay vì paperweight để build nhẹ và dễ hơn cho người mới
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    // Thư viện xử lý lệnh
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    // Thư viện quản lý Config
    implementation("dev.dejvokep:boosted-yaml:1.3.6")

    // Lombok (Cần thiết cho code Java tôi đã đưa)
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks {
    shadowJar {
        // Gom tất cả thư viện vào 1 file .jar duy nhất
        archiveClassifier.set("")
        relocate("co.aikar.commands", "vn.yourname.disableenchant.libs.acf")
        relocate("dev.dejvokep.boostedyaml", "vn.yourname.disableenchant.libs.boostedyaml")
    }
    
    build {
        dependsOn(shadowJar)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
