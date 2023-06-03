package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhenov.librarianapp.models.Book;
import ru.bazhenov.librarianapp.models.Reader;
import ru.bazhenov.librarianapp.repositories.ReaderRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ReaderService {
    private final ReaderRepository readerRepository;
    private final static int periodLimit = 10;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public List<Reader> index() {
        return readerRepository.findAll();
    }

    public boolean findReaderByName(String name){
        return readerRepository.findByFullName(name).isPresent();
    }
    public boolean findReaderByBirth(Date date){
        return readerRepository.findByYearOfBirth(date).isPresent();
    }

    public Reader getByIdReader(int id) {

        Reader reader = readerRepository.findById(id).orElse(null);
        for (Book book : Objects.requireNonNull(reader).getBookList()) {
            if (gePeriodBookTaken(book.getBookTakenDate()) >= periodLimit) {
                book.setBookTakenIsExpired(true);
            }
        }
        return reader;
    }

    private Long gePeriodBookTaken(Date dateTaken) {
        LocalDate localDate = LocalDate.now();
        LocalDate bookTakenDate = dateTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(bookTakenDate, localDate);
    }

    @Transactional
    public void addReader(Reader reader) {
        readerRepository.save(reader);
    }

    @Transactional
    public void updateReader(int id, Reader updatedReader) {
        updatedReader.setId(id);
        readerRepository.save(updatedReader);
    }

    @Transactional
    public void deleteReader(int id) {
        Reader reader = readerRepository.findById(id).orElse(null);
        Objects.requireNonNull(reader).getBookList().forEach(book -> book.setBookTakenDate(null));
        readerRepository.deleteById(id);
    }
}
