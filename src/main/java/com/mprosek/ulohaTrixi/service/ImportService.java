package com.mprosek.ulohaTrixi.service;

import com.mprosek.ulohaTrixi.entity.CastObce;
import com.mprosek.ulohaTrixi.entity.Obec;
import com.mprosek.ulohaTrixi.repository.CastObceRepository;
import com.mprosek.ulohaTrixi.repository.ObecRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ImportService implements CommandLineRunner {
    private final ObecRepository obecRepository;
    private final CastObceRepository castObceRepository;

    public ImportService(ObecRepository obecRepository, CastObceRepository castObceRepository) {
        this.obecRepository = obecRepository;
        this.castObceRepository = castObceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String zipUrl = "https://www.smartform.cz/download/kopidlno.xml.zip";
        System.out.println("Zahajuji stahování a parsování dat...");

        // 1. Otevření spojení a stažení ZIP archivu
        try (ZipInputStream zis = new ZipInputStream(new URL(zipUrl).openStream())) {
            
            ZipEntry entry = zis.getNextEntry();
            if (entry != null) { // Pokud ZIP obsahuje soubor
                
                // 2. Příprava DOM parseru
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                
                // Parser načte rozbalená data přímo ze streamu (nemusíme soubor ukládat na disk)
                Document document = builder.parse(zis);
                
                // 3. Zpracování Obce
                NodeList obecNodes = document.getElementsByTagName("vf:Obec");
                for (int i = 0; i < obecNodes.getLength(); i++) {
                    Element obecElement = (Element) obecNodes.item(i);
                    
                    Integer kod = Integer.parseInt(getTextContent(obecElement, "obi:Kod"));
                    String nazev = getTextContent(obecElement, "obi:Nazev");
                    
                    obecRepository.save(new Obec(kod, nazev));
                }

                // 4. Zpracování Částí obce
                NodeList castiObceNodes = document.getElementsByTagName("vf:CastObce");
                for (int i = 0; i < castiObceNodes.getLength(); i++) {
                    Element castElement = (Element) castiObceNodes.item(i);
                    
                    Integer kod = Integer.parseInt(getTextContent(castElement, "coi:Kod"));
                    String nazev = getTextContent(castElement, "coi:Nazev");
                    
                    // Kód obce je zanořený v elementu <coa:Obec> -> <coi:Kod>
                    Element obecVnitrniElement = (Element) castElement.getElementsByTagName("coi:Obec").item(0);
                    Integer kodObce = Integer.parseInt(getTextContent(obecVnitrniElement, "obi:Kod"));
                    
                    castObceRepository.save(new CastObce(kod, nazev, kodObce));
                }
                
                System.out.println("Data byla úspěšně uložena do databáze.");
            }
        }
    }

    // Pomocná metoda pro bezpečné vytažení textu z elementu
    private String getTextContent(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}
