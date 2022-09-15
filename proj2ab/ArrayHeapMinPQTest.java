package bearmaps;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {

    @Test
    public void testAdd() {
        ArrayHeapMinPQ<String> test = new ArrayHeapMinPQ<>();
        test.add("a", 2);
        test.add("b", 5);
        test.add("c", 1);
        test.add("d", 4);
        test.add("e", 2);

        //remove and getSmallest test
        System.out.println(test.getSmallest());
        test.removeSmallest();
        System.out.println(test.getSmallest());
        test.removeSmallest();
        System.out.println(test.getSmallest());
        test.removeSmallest();
        System.out.println(test.getSmallest());
    }

    @Test
    public void testAdds() {
        ArrayHeapMinPQ<String> heap = new ArrayHeapMinPQ<>();
        assertEquals(0, heap.size());
        int test = 200000;
        for (int i = 0; i < test; i++) {
            heap.add("Noe" + i, i);
        }
        assertEquals("Noe0", heap.getSmallest());
        assertEquals(test, heap.size());
    }

    @Test
    public void testRemove() {
        ArrayHeapMinPQ<String> heap = new ArrayHeapMinPQ<>();
        int test = 100000;
        for (int i = 0; i < test; i++) {
            heap.add("Noe" + i, i);
        }
        for (int i = 0; i < test - 1; i++) {
            heap.removeSmallest();
        }
        assertEquals("Noe99999", heap.getSmallest());
        assertEquals(1, heap.size());
    }

    @Test
    public void testChange() {
        ArrayHeapMinPQ<String> c = new ArrayHeapMinPQ<>();
        int test = 550000;
        for (int i = 0; i < test; i++) {
            c.add("Cebreros" + i, i);
        }
        for (int i = 0; i < test; i++) {
            c.changePriority("Cebreros" + i, test - 1 - i);
        }
        for (int i = 0; i < test; i++) {
            assertEquals("Cebreros" + (test - 1 - i), c.removeSmallest());
        }
    }
}
