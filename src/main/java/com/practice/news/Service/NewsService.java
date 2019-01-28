package com.practice.news.Service;

import com.practice.news.Error.Invalid;
import com.practice.news.Model.News;
import com.practice.news.Persistence.NewsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

	private NewsRepository newsRepository;

	@Autowired
	public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	public List<News> findAllByOrderByIdDesc() {
		Optional<List<News>> news = newsRepository.findAllByOrderByIdDesc();
		return news.isPresent() ? news.get() : Arrays.asList(
				new News("No news available", "-",
						"-", new Date()));

	}

	public Page<News> findAllByOrderByDateDesc(Pageable pageable) {
		Optional<Page<News>> news = newsRepository.findAllByOrderByDateDesc(pageable);
		return news.get() ;

	}


	public News findById(Long id) {
		Optional<News> news = newsRepository.findById(id);
		news.orElseThrow(() -> new Invalid("Id not found"));
		System.out.println("News got " + news.get().getId());
		return news.get();
	}

	public News findById(String id) {
		try {
			return findById(Long.parseLong(id));
		} catch (Exception e) {
			throw new Invalid("Invalid");
		}
	}

	public void save(News news, BindingResult bindingResult) {
		isValid(bindingResult);
		newsRepository.saveAndFlush(news);
	}
	public void delete(String id){
		newsRepository.delete(findById(id));
	}

	private void isValid(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			StringBuilder errMsg = new StringBuilder();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errMsg.append(fieldError.getDefaultMessage() + "\n");
			}
			throw new Invalid(errMsg.toString());
		}
	}
}
