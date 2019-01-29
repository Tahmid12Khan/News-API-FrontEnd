package com.practice.news.Model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Size(min = 1, max = 50, message = "Title can't be empty or have more than 50 characters")
	private String title;
	@Column(columnDefinition = "MEDIUMTEXT")

	@Size(min = 1, max = 5000, message = " Body can't be empty or have more than 5000 characters")
	private String body;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Size(min = 1, max = 50, message = "Name can't be empty or have more than 50 characters")
	private String author;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Date can't be empty")
	private Date date;

	public News() {

	}

	public News(long id) {
		this.id = id;
	}

	public News(String title, String body, String author, Date date) {
		this.title = title;
		this.body = body;
		this.author = author;
		this.date = date;
	}

	public News(String author) {
		this.author = author;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String showFewCharacters() {
		return body.length() <= 100 ? body : body.substring(0, 99) + "...";
	}

	public String dateFormat() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(date);
		System.out.println("Date: " + strDate);
		return strDate;
	}
}
