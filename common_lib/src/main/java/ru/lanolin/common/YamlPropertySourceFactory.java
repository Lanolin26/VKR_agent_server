package ru.lanolin.common;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Objects;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {
	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		Resource resource = encodedResource.getResource();

		factory.setResources(resource);
		Properties properties = Objects.requireNonNull(factory.getObject());

		return new PropertiesPropertySource(Objects.requireNonNull(resource.getFilename()), properties);
	}
}
