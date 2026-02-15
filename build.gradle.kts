plugins {
    java
    id("io.papermc.paper.weight.userdev") version "1.7.1" // Plugin hỗ trợ NMS/Paper tốt nhất
    id("xyz.jpenilla.run-paper") version "2.3.0" // Hỗ trợ chạy test server nhanh
    id("net.kyori.blossom") version "1.3.1" // Thay thế token lúc build
}

group = "oj.nuoccam.disableenchant"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/") // Repo cho ACF
    maven("https://jitpack.io") // Repo cho BoostedYAML (nếu cần) hoặc MavenCentral
}

dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT") // API 1.21 mới nhất

    // 1. Lib: ACF (Command Framework)
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    // 2. Lib: BoostedYAML (Config xịn)
    implementation("dev.dejvokep:boosted-yaml:1.3.6")

    // 3. Lib: Lombok (Code gọn)
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21)) // Minecraft 1.21 yêu cầu Java 21
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
    
    // ShadowJar để đóng gói các thư viện (ACF, BoostedYAML) vào trong plugin .jar cuối cùng
    // Bạn cần thêm plugin shadow vào block plugins ở trên nếu muốn tự config, 
    // nhưng paperweight đã hỗ trợ reobf, ta dùng task build mặc định,
    // tuy nhiên để bundle libs, ta nên dùng ShadowJar plugin.
    // Để đơn giản cho bạn, tôi sẽ dùng cấu hình cơ bản compileJava, 
    // nhưng thực tế bạn cần plugin "com.github.johnrengelman.shadow" version "8.1.1".
}

// Thêm block này vào đầu file plugins {} nếu bạn chưa có
/*
plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    ... các plugin khác
}
*/
// Sau đó cấu hình build:
tasks.shadowJar {
    minimize() // Loại bỏ code thừa trong thư viện
    relocate("co.aikar.commands", "vn.yourname.disableenchant.libs.acf")
    relocate("dev.dejvokep.boostedyaml", "vn.yourname.disableenchant.libs.boostedyaml")
    archiveClassifier.set("") // Tên file output gọn hơn
}