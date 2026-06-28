import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

tasks.register("generateTvBanner") {
    notCompatibleWithConfigurationCache("Requires project runtime references")
    doLast {
        val width = 320
        val height = 180
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g = img.createGraphics()
        
        val imgSize = 120
        val xOffset = 20
        val yOffset = (height - imgSize) / 2
        
        // Background color #1C203B
        g.color = Color(0x1C, 0x20, 0x3B)
        g.fillRect(0, 0, width, height)
        
        // Draw image
        val routerImageFile = File(project.rootDir, "app/src/main/res/drawable/xiaomi_ax3000t_icon_1779873723654.png")
        if (routerImageFile.exists()) {
            val routerImg = ImageIO.read(routerImageFile)
            
            // Scale and draw router image on the left
            g.drawImage(routerImg, xOffset, yOffset, imgSize, imgSize, null)
        }
        
        // Draw text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.color = Color.WHITE
        g.font = Font("Roboto Flex", Font.PLAIN, 25)
        
        val lines = listOf("OpenWrt", "Router", "Control")
        val fm = g.fontMetrics
        val textX = 160
        var textY = (height - (lines.size * fm.height)) / 2 + fm.ascent
        
        for (line in lines) {
            g.drawString(line, textX, textY)
            textY += fm.height
        }
        
        g.dispose()
        
        val outputDir = File(project.rootDir, "app/src/main/res/drawable-xhdpi")
        if (!outputDir.exists()) outputDir.mkdirs()
        
        val outputFile = File(outputDir, "tv_banner.png")
        ImageIO.write(img, "png", outputFile)
        println("Generated TV banner at ${outputFile.absolutePath}")
    }
}
