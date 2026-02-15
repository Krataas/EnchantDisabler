plugins {
    java
    // Nâng cấp lên phiên bản 8.3.5 để tương thích với Gradle mới trên GitHub
    id("com.github.johnrengelman.shadow") version "8.3.5"
}

group = "oj.nuoccam.disableenchant"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    
    // Thư viện cần thiết
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("dev.dejvokep:boostedyaml:1.3.6")

    // Lombok để code gọn gàng
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        
        // Di dời thư viện để tránh xung đột với plugin khác (Relocation)
        relocate("co.aikar.commands", "oj.nuoccam.disableenchant.libs.acf")
        relocate("dev.dejvokep.boostedyaml", "oj.nuoccam.disableenchant.libs.boostedyaml")

        // Giải pháp cho lỗi bạn vừa gặp: Loại bỏ các file trùng lặp gây lỗi build
        mergeServiceFiles()
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }

    // Đảm bảo khi chạy lệnh 'build' nó sẽ chạy 'shadowJar'
    build {
        dependsOn(shadowJar)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
