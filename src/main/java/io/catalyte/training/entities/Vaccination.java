package io.catalyte.training.entities;

import static io.catalyte.training.constants.StringConstants.REQUIRED_FIELD;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Vaccination {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "innoculation" + REQUIRED_FIELD)
  private String innoculation;

  @NotNull(message = "date" + REQUIRED_FIELD)
  @JsonFormat(pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date date;

  @ManyToOne
  @NotNull
  private Pet pet;

  public Vaccination() {
  }

  public Vaccination(
      @NotBlank(message = "Title" + REQUIRED_FIELD) String innoculation,
      @NotBlank(message = "Date" + REQUIRED_FIELD) Date date,
      @NotNull(message = "Pet" + REQUIRED_FIELD) Pet pet) {
    this.innoculation = innoculation;
    this.date = date;
    this.pet = pet;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getInnoculation() {
    return innoculation;
  }

  public void setInnoculation(String innoculation) {
    this.innoculation = innoculation;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Pet getPet() {
    return pet;
  }

  public void setPet(Pet pet) {
    this.pet = pet;
  }

  @Override
  public String toString() {
    return "Vaccination{" +
        "id=" + id +
        ", innoculation='" + innoculation + '\'' +
        ", date=" + date +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Vaccination)) {
      return false;
    }
    Vaccination that = (Vaccination) o;
    return id.equals(that.id) &&
        innoculation.equals(that.innoculation) &&
        date.equals(that.date) &&
        pet.equals(that.pet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, innoculation, date, pet);
  }

  @JsonIgnore
  public boolean isEmpty() {
    return Objects.isNull(id) &&
        Objects.isNull(innoculation) &&
        Objects.isNull(date) &&
        Objects.isNull(pet);
  }
}