package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Entity
@Table(name = "comittee", schema = "ivs")
public class Committee {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(name = "comitteeName", nullable = false)
  private String comitteeName;

  @Column(name = "comitteeDescription", nullable = false)
  private String comitteeDescription;

  @OneToMany(mappedBy = "committee")
  private Set<Puk> puks;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "comittee_role_head_id", referencedColumnName = "id")
  private Role comitteeRoleHead;

  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public Committee(long id, String comitteeName, String comitteeDescription, Set<Puk> puks, Role comitteeRoleHead) {
    this.id = id;
    this.comitteeName = comitteeName;
    this.comitteeDescription = comitteeDescription;
    this.puks = puks;
    this.comitteeRoleHead = comitteeRoleHead;
  }

  public long getId() {
    return id;
  }

  public String getComitteeName() {
    return comitteeName;
  }

  public String getComitteeDescription() {
    return comitteeDescription;
  }

  public Set<Puk> getPuks() {
    return puks;
  }

  public Role getComitteeRoleHead() {
    return comitteeRoleHead;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }
}
