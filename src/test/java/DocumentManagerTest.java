import com.alexpoty.Test_Task.DocumentManager;
import com.alexpoty.Test_Task.DocumentManager.Document;
import com.alexpoty.Test_Task.DocumentManager.Author;
import org.junit.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentManagerTest {

    private final DocumentManager documentManager = new DocumentManager();

    @Test
    public void should_save_document() {
        Document document = Document.builder()
                .title("Test Title")
                .content("Test Content")
                .author(Author.builder()
                        .id("123")
                        .name("John Doe")
                        .build())
                .created(Instant.now())
                .build();

        Document savedDocument = documentManager.save(document);

        assertNotNull(savedDocument.getId());
        assertEquals("Test Title", savedDocument.getTitle());
    }

    @Test
    public void should_find_document_by_id() {
        Document document = Document.builder()
                .id("doc-1")
                .title("Test Title")
                .content("Content")
                .author(Author.builder()
                        .id("456")
                        .name("Alice")
                        .build())
                .created(Instant.now())
                .build();

        documentManager.save(document);
        Optional<Document> actual = documentManager.findById("doc-1");

        assertTrue(actual.isPresent());
        assertEquals("Test Title", actual.get().getTitle());
    }

    @Test
    public void should_find_documents_by_request() {
        Document doc1 = documentManager.save(Document.builder()
                .title("Test Document 1")
                .content("Test Content 1")
                .author(Author.builder()
                        .id("111")
                        .name("Bob")
                        .build())
                .created(Instant.now())
                .build());

       Document doc2 = documentManager.save(Document.builder()
                .title("Test Document 2")
                .content("Test Content 2")
                .author(Author.builder()
                        .id("222")
                        .name("Charlie")
                        .build())
                .created(Instant.now())
                .build());

        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Test Document 1"))
                .authorIds(List.of("111"))
                .containsContents(List.of("Test Content 1"))
                .build();

        List<Document> results = documentManager.search(request);

        assertEquals(1, results.size());
        assertEquals("Test Document 1", results.getFirst().getTitle());
    }
}
