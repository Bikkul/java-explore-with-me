package ru.practicum.ewm.stats.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "hits")
@AllArgsConstructor
@NoArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String app;

    @Column(nullable = false, length = 255)
    private String uri;

    @Column(nullable = false, length = 16)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Hit hit = (Hit) o;
        return getId() != null && Objects.equals(getId(), hit.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}