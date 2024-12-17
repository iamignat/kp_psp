package ignat.malko.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "sender_id")
    private Account sender;
    @ManyToOne()
    @JoinColumn(name = "receiver_id")
    private Account receiver;
    private double amount;
    private LocalDate date;
    private String message;
}
