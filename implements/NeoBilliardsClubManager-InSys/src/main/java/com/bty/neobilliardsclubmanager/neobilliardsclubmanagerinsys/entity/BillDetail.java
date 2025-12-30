package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "BillDetail",
    uniqueConstraints = {
        @UniqueConstraint(
                name = "IX_BillDetail",
                columnNames = {"BillId", "ProductId"}
        )
    }
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillDetail {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "Quantity", nullable = false)
    Long quantity;

    @ManyToOne
    @JoinColumn(name = "BillId")
    Bill bill;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    Product product;
}
