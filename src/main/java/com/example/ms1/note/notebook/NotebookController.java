package com.example.ms1.note.notebook;

import com.example.ms1.note.MainService;
import com.example.ms1.note.ParamHandler;
import com.example.ms1.note.note.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;
    private final MainService mainService;

    @PostMapping("/books/write")
    public String write(ParamHandler paramHandler) {
        mainService.saveDefaultNotebook();
        return paramHandler.getRedirectUrl("/");

    }

    @PostMapping("/groups/{notebookId}/books/write")
    public String groupWrite(@PathVariable("notebookId") Long notebookId, ParamHandler paramHandler) {

        mainService.saveGroupNotebook(notebookId);
        Notebook notebook = this.notebookService.getNotebook(notebookId);
        Long lastChildId = notebook.getChildren().getLast().getId();

        return paramHandler.getRedirectUrl("/books/%d".formatted(lastChildId));
    }

    @GetMapping("/books/{id}")
    public String detail(@PathVariable("id") Long id, ParamHandler paramHandler) {

        Notebook notebook = notebookService.getNotebook(id);

        if (notebook.getNoteList().isEmpty()) {
            this.mainService.addToNotebook(id);
            return paramHandler.getRedirectUrl("/books/%d".formatted(id));
        }
        else {
            Note note = notebook.getNoteList().getLast();

            return paramHandler.getRedirectUrl("/books/%d/notes/%d".formatted(id, note.getId()));
        }
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable("id") Long id, ParamHandler paramHandler) {
        Notebook notebook = this.notebookService.getNotebook(id);

        notebookService.delete(id);

        if (notebook.getParent() != null) {
            Long parentId = notebook.getParent().getId();
            return paramHandler.getRedirectUrl("/books/%d".formatted(parentId));
        }

        return paramHandler.getRedirectUrl("/");
    }

    @PostMapping("/books/{id}/update")
    public String update (@PathVariable("id")Long id,Long targetNoteId, String name, ParamHandler paramHandler) {
        this.notebookService.update(id, name);

        return paramHandler.getRedirectUrl("/books/%d/notes/%d".formatted(id, targetNoteId));
    }

    @PostMapping("/books/{id}/move")
    public String move (@PathVariable("id")Long id, Long destinationId, Long targetNoteId, ParamHandler paramHandler) {

        if (destinationId == null) {
            return paramHandler.getRedirectUrl("/books/%d/notes/%d".formatted(id, targetNoteId));
        }

        this.notebookService.move(id, destinationId);

        return paramHandler.getRedirectUrl("/books/%d/notes/%d".formatted(id, targetNoteId));
    }

}
