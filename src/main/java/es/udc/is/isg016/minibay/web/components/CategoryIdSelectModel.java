package es.udc.is.isg016.minibay.web.components;

import java.util.List;
import java.util.ArrayList;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

import es.udc.is.isg016.minibay.model.category.Category;

public class CategoryIdSelectModel extends AbstractSelectModel{

	private List<Category> list;
	
	public CategoryIdSelectModel(List<Category> l){
		list = l;
	}
	
	@Override
	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	@Override
	public List<OptionModel> getOptions() {
		List<OptionModel> options = new ArrayList<OptionModel>();
		for(Category c : list){
			options.add(new OptionModelImpl(c.getCategoryName(),c.getCategoryId()));
		}
		return options;
	}

	
	
}
