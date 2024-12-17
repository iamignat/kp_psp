package ignat.malko.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


@Data
public class Transaction implements Serializable {
    private Long id;
    private Account sender;
    private Account receiver;
    private double amount;
    private LocalDate date;
    private String message;
}
