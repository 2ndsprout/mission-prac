package com.example.ms1.note;

import com.example.ms1.note.note.Note;
import com.example.ms1.note.notebook.Notebook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MainDataDto {
    List<Notebook> notebookList;
    Notebook targetNotebook;
    List<Note> noteList;
    Note targetNote;
    List<Notebook> searchedNotebookList;
    List<Note> searchedNoteList;
}
