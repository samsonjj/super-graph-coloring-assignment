public class Junk {
/*public int[] initialColor() {
        Arrays.fill(colors, 0);

        // Welsh-Powell Algorithm
        Arrays.sort(adjacencyList, (a1, a2) -> a2.size() - a1.size());
        Arrays.stream(adjacencyList).forEach(System.out::println);

        boolean[] availableColors = new boolean[adjacencyList.length + 1];
        for(int i = 0; i < adjacencyList.length; i++) {

            // reset available colors
            Arrays.fill(availableColors, true);

            // set available colors
            for(Integer adjacent : adjacencyList[i]) {
                if(colors[adjacent] != 0) availableColors[colors[adjacent]] = false;
            }

            // find first available color
            int firstAvailable = 1;
            while(!availableColors[firstAvailable]) {
                firstAvailable++;
            }

            // set color of i to that
            colors[i] = firstAvailable;
        }

        return colors;
    }*/

}
