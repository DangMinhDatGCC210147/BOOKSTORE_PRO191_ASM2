package JUnitTest;

import Lib.XFile;
import Model.Category;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CategoryGUITest {
    String filePathTest = "src/File/category.dat";
    List<Category> categoryList;
    @Before
    public void setUp() throws Exception {
        categoryList = (List<Category>) XFile.readObject(filePathTest);
    }
    @Test
    public void testAdd(){
        int size = categoryList.size();
        Category cate = new Category("CAT001", "Adventure");
        categoryList.add(cate);
        int expectedInput = size + 1;
        assertEquals(expectedInput, categoryList.size());
    }
    @Test
    public void testRemove(){
        Category ca = null;
        Category cat = new Category("CAT001", "Adventure");
        categoryList.add(cat);
        int size = categoryList.size();
        for (Category cate : categoryList){
            if (cate.getCategoryId().equals("CAT001")){
                ca = cate;
            }
        }categoryList.remove(ca);
        int expectedOutput = size - 1;
        assertEquals(expectedOutput, categoryList.size());
    }

    @Test
    public void testRemoveAll(){
        categoryList.removeAll(categoryList);
        int expectedOutput = 0;
        assertEquals(expectedOutput, categoryList.size());
    }

    @Test
    public void testDuplicate(){

    }
}