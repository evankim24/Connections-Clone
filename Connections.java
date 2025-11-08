import tester.*;                
import javalib.worldimages.*;   
import javalib.impworld.*;      
import java.awt.Color;                                        
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

// represents a group of words connected by a category
class WordGroup {
  ArrayList<String> words;
  String category;
  Color groupColor;
  
  WordGroup(ArrayList<String> words, String category, Color groupColor) {
    this.words = words;
    this.category = category;
    this.groupColor = groupColor;
  }
}

// represents the connections board and all its methods
class ConnectionsWorld extends World {
  ArrayList<String> currentGame;
  ArrayList<Integer> selectedIndices;
  ArrayList<WordGroup> wordGroups;
  ArrayList<WordGroup> solvedGroups;
  String message;
  Color messageColor;
  Random rand;
  ArrayList<ArrayList<WordGroup>> allWordSets;
  int remainingAttempts;
  int totalAttempts;
  boolean showPartialMatches;
  boolean gameHasEnded = false;
  String finalMessage = "";
  boolean victoryScreenShown = false;
  
  //  Modified constructor
  ConnectionsWorld(Random rand) {
    this.rand = rand;
    this.currentGame = new ArrayList<String>();
    this.selectedIndices = new ArrayList<Integer>();
    this.wordGroups = new ArrayList<WordGroup>();
    this.solvedGroups = new ArrayList<WordGroup>();
    this.message = "";
    this.messageColor = DEFAULT_COLOR;
    this.allWordSets = new ArrayList<ArrayList<WordGroup>>();
    this.remainingAttempts = 4;
    this.totalAttempts = 0;
    this.showPartialMatches = false;
    initWords();
    selectWordSet(); 
  }
  
  // Keep original constructor
  ConnectionsWorld() {
    this(new Random());
    this.shuffleCurrentGame(); // Shuffle only in default constructor
  }
  
  // constants
  static final int BACKGROUND_HEIGHT = 600;
  static final int BACKGROUND_WIDTH = 800;
  static final int GRID_ROWS = 4;
  static final int GRID_COLUMNS = 4;
  static final int WORD_BOX_HEIGHT = 150;
  static final int WORD_BOX_WIDTH = 200;
  static final int FONT_SIZE = 16;
  static final Color SELECTED_COLOR = new Color(70, 70, 200);
  static final Color DEFAULT_COLOR = Color.BLACK;
  static final Color PARTIAL_MATCH_COLOR = new Color(255, 165, 0); 
  static final Color BACKGROUND_COLOR = new Color(240, 240, 240); 
  static final Color BOX_BACKGROUND = new Color(255, 255, 255);
  
  // Initializes all the possible word sets
  void initWords() {
    this.allWordSets = new ArrayList<ArrayList<WordGroup>>();
    
    ArrayList<WordGroup> set1 = new ArrayList<WordGroup>();
    ArrayList<WordGroup> set2 = new ArrayList<WordGroup>();
    ArrayList<WordGroup> set3 = new ArrayList<WordGroup>();
    ArrayList<WordGroup> set4 = new ArrayList<WordGroup>();
    ArrayList<WordGroup> set5 = new ArrayList<WordGroup>();
    
    set1.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("HAIL", "RAIN", "SLEET", "SNOW")), "WET WEATHER", Color.YELLOW));
    set1.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("BUCKS", "HEAT", "JAZZ", "NETS")), "NBA TEAMS", Color.GREEN)); 
    set1.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("OPTION", "RETURN", "SHIFT", "TAB")), "KEYBOARD KEYS", Color.BLUE)); 
    set1.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("KAYAK", "LEVEL", "MOM", "RACECAR")), "PALINDROMES", 
        new Color(102, 0, 153))); 
    
    set2.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("CHEEK", "EYE", "MOUTH", "NOSE")), "FACIAL FEATURES", Color.YELLOW));
    set2.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("CHOW", "GOBBLE", "SCARF", "WOLF")), "SYNONYMS FOR EAT", Color.GREEN));
    set2.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("LAB", "PEKE", "PIT", "POM")), "DOG BREEDS, INFORMALLY", Color.BLUE));
    set2.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("AMIGO", "KING", "STOOGE", "TENOR")), "MEMBERS OF A TRIO", 
        new Color(102, 0, 153)));
    
    set3.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("BOOT", "LOAFER", "PUMP", "SNEAKER")), "FOOTWEAR", Color.YELLOW));
    set3.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("FOOT", "LEAGUE", "MILE", "YARD")), "UNITS OF LENGTH", Color.GREEN));
    set3.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("ESSENCE", "PEOPLE", "TIME", "US")), "MAGAZINES", Color.BLUE));
    set3.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("ARE", "QUEUE", "SEA", "WHY")), "LETTER HOMOPHONES", 
        new Color(102, 0, 153)));
    
    set4.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("ADIDAS", "NIKE", "PUMA", "REEBOK")), "SNEAKER BRANDS", Color.YELLOW));
    set4.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("CABARET", "CAROUSEL", "CATS", "CHICAGO")), "MUSICALS BEGINNING WITH C", 
        Color.GREEN));
    set4.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("DUST", "MOP", "SWEEP", "VACUUM")), "CLEANING VERBS", Color.BLUE));
    set4.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("BAT", "IRON", "SPIDER", "SUPER")), "___ MAN SUPERHEROES", 
        new Color(102, 0, 153)));
    
    set5.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("HULU", "NETFLIX", "PEACOCK", "PRIME")), "STREAMING SERVICES", Color.YELLOW));
    set5.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("BUCKS", "HEAT", "JAZZ", "NETS")), "NBA TEAMS", Color.GREEN));
    set5.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("BLUE", "DOWN", "GLUM", "LOW")), "SYNONYMS FOR SAD", Color.BLUE));
    set5.add(new WordGroup(new ArrayList<String>(
        Arrays.asList("GREEN", "MUSTARD", "PLUM", "SCARLET")), "CLUE CHARACTERS", 
        new Color(102, 0, 153)));
    
    this.allWordSets.add(set1);
    this.allWordSets.add(set2);
    this.allWordSets.add(set3);
    this.allWordSets.add(set4);
    this.allWordSets.add(set5);
  }
  
  // FOR SHUFFLECURRENTGAME TEST THIS IS NOT A REAL METHOD
  void shuffleCurrentGame(Random rand) {  
    Collections.shuffle(this.currentGame, rand);
  }

  // Shuffles the words around for the game
  void shuffleCurrentGame() {
    shuffleCurrentGame(this.rand); 
  }

  // Selects a random group of words to use for the game
  void selectWordSet() {
    ArrayList<WordGroup> selectedSet = 
        this.allWordSets.get(this.rand.nextInt(this.allWordSets.size()));
    this.wordGroups = selectedSet;
    
    this.currentGame = new ArrayList<String>();
    for (WordGroup group : selectedSet) {
      this.currentGame.addAll(group.words);
    }
  }
  
  // draws the current state of the world
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    scene.placeImageXY(
        new RectangleImage(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 
            OutlineMode.SOLID, BACKGROUND_COLOR),
        BACKGROUND_WIDTH / 2, BACKGROUND_HEIGHT / 2);
    
    for (int i = 0; i < this.currentGame.size(); i++) {
      drawWordBox(scene, i);
    }
    
    scene.placeImageXY(
        new TextImage("Press: [Enter] Submit [Esc] Clear [S] Shuffle [D] Deselect", 
            FONT_SIZE - 2, Color.DARK_GRAY),
        BACKGROUND_WIDTH / 2, BACKGROUND_HEIGHT - 10);
        
    drawGameStatus(scene);
    return scene;
  }
  
  // Helper to draw individual word box
  void drawWordBox(WorldScene scene, int wordIndex) {
    String word = this.currentGame.get(wordIndex);
    Color color = getWordColor(wordIndex);
    
    Color bgColor = BOX_BACKGROUND;
    if (selectedIndices.contains(wordIndex)) {
      bgColor = new Color(230, 230, 255);
    }
    
    WorldImage box = new OverlayImage(
        new RectangleImage(WORD_BOX_WIDTH - 10, WORD_BOX_HEIGHT - 10,
            OutlineMode.SOLID, bgColor),
        new RectangleImage(WORD_BOX_WIDTH - 8, WORD_BOX_HEIGHT - 8,
            OutlineMode.OUTLINE, color)
    );
    WorldImage text = new TextImage(word, FONT_SIZE, color);
    WorldImage wordImage = new OverlayImage(text, box);
    
    int col = wordIndex % GRID_COLUMNS;
    int row = wordIndex / GRID_COLUMNS;
    int x = col * WORD_BOX_WIDTH + (WORD_BOX_WIDTH / 2);
    int y = row * WORD_BOX_HEIGHT + (WORD_BOX_HEIGHT / 2);
    
    scene.placeImageXY(wordImage, x, y);
  }
  
  // Helper to determine word color
  Color getWordColor(int index) {
    if (index < 0 || index >= currentGame.size()) {
      return DEFAULT_COLOR;
    }
    
    if (selectedIndices.contains(index)) {
      return SELECTED_COLOR;
    }
    
    String word = currentGame.get(index);
    for (WordGroup group : solvedGroups) {
      if (group.words.contains(word)) {
        return group.groupColor;
      }
    }
    
    return DEFAULT_COLOR;
  }
  
  // Helper to draw game status
  void drawGameStatus(WorldScene scene) {
    WorldImage status = new TextImage(
        "Groups: " + this.solvedGroups.size() + "/" + this.wordGroups.size(),
        FONT_SIZE + 2, Color.BLACK);
    scene.placeImageXY(status, BACKGROUND_WIDTH - 100, BACKGROUND_HEIGHT - 30);
    
    if (!this.message.isEmpty()) {
      WorldImage msg = new TextImage(this.message, FONT_SIZE, this.messageColor);
      scene.placeImageXY(msg, BACKGROUND_WIDTH / 2, BACKGROUND_HEIGHT - 30);
    }
  }
  
  // calculates what the word is at the specific position
  public void onMouseClicked(Posn pos) {
    int clickedIndex = getWordIndexAt(pos.x, pos.y);
    
    if (clickedIndex != -1) {
      toggleSelection(clickedIndex);
    }
  }
  
  // on key for game, escape, s, d and enter
  public void onKeyEvent(String key) {
    if (key.equals("enter")) {
      checkSelectedGroup();
    }
    else if (key.equals("escape")) {
      this.selectedIndices.clear();
      this.message = "Selection cleared";
    }
    else if (key.equals("s")) {
      this.shuffleCurrentGame();
      this.message = "Words shuffled";
    }
    else if (key.equals("d")) {
      this.selectedIndices.clear();
      this.message = "Selection cleared";
    }
  }
  
  // sees if the selected indices are correct and need to be removed
  void toggleSelection(int index) {
    if (index < 0 || index >= currentGame.size()) {
      return;
    }
    
    if (this.selectedIndices.contains(index)) {
      this.selectedIndices.remove((Integer) index);
    } 
    else if (this.selectedIndices.size() < 4) {
      this.selectedIndices.add(index);
    }
  }
  
  // the last scene of the game
  public WorldScene lastScene(String msg) {
    WorldScene scene = this.makeScene();
    
    Color textColor = msg.contains("Perfect") ? Color.GREEN :
                     msg.contains("Great") ? Color.BLUE :
                     msg.contains("Good") ? Color.ORANGE : Color.RED;
    
    WorldImage result = new OverlayImage(
        new TextImage(msg, 32, textColor),
        new RectangleImage(500, 100, OutlineMode.SOLID, new Color(255, 255, 255, 200))
    );
    
    scene.placeImageXY(result, BACKGROUND_WIDTH / 2, BACKGROUND_HEIGHT / 2);
    this.victoryScreenShown = true;
    return scene;
  }
  
  // returns index at certain ints
  int getWordIndexAt(int x, int y) {
    if (x < 0 || y < 0 || x >= GRID_COLUMNS * WORD_BOX_WIDTH 
                       || y >= GRID_ROWS * WORD_BOX_HEIGHT) {
      return -1;
    }
    
    int col = Math.min(x / WORD_BOX_WIDTH, GRID_COLUMNS - 1);
    int row = Math.min(y / WORD_BOX_HEIGHT, GRID_ROWS - 1);
    int index = row * GRID_COLUMNS + col;
    
    return (index < currentGame.size()) ? index : -1;
  }
  
  // checks for selected size and if it is correct or how many are correct
  void checkSelectedGroup() {
    if (this.selectedIndices.size() != 4) {
      this.message = "Select exactly 4 words";
      this.messageColor = Color.RED;
      return;
    }

    this.totalAttempts++;
    ArrayList<String> selectedWords = getSelectedWords();
    WordGroup matchedGroup = findMatchingGroup(selectedWords);

    if (!matchedGroup.category.isEmpty() && !this.solvedGroups.contains(matchedGroup)) {
      this.solvedGroups.add(matchedGroup);
      this.message = "Correct! " + matchedGroup.category;
      this.messageColor = matchedGroup.groupColor;
      checkGameEnd();
    } 
    else {
      this.remainingAttempts--;
      
      int maxMatches = findMaxMatchingWords(selectedWords);
      if (maxMatches >= 3) {
        this.message = "Close! " + maxMatches + "/4 correct ("
            + this.remainingAttempts + " tries left)";
        this.messageColor = PARTIAL_MATCH_COLOR;
      } 
      else {
        this.message = "Incorrect! " + this.remainingAttempts + " tries left";
        this.messageColor = Color.RED;
      }
      
      if (this.remainingAttempts <= 0) {
        this.endOfWorld("Game Over - You ran out of tries");
      }
    }
    this.selectedIndices.clear();
  }
  
  // helper to find how close the player is to finding a group
  int findMaxMatchingWords(ArrayList<String> selectedWords) {
    int max = 0;
    for (WordGroup group : this.wordGroups) {
      if (solvedGroups.contains(group)) {
        continue;
      }
      int count = 0;
      for (String word : selectedWords) {
        if (group.words.contains(word)) {
          count++;
        }
      }
      if (count > max) { 
        max = count;
      }
    }
    return max;
  }
  
  // sees if the game is over (correct win)
  void checkGameEnd() {
    if (this.solvedGroups.size() == this.wordGroups.size()) {
      this.gameHasEnded = true;
      if (this.totalAttempts <= wordGroups.size()) {
        this.finalMessage = "Perfect! Solved in " + this.totalAttempts + " attempts";
      } 
      else if (this.totalAttempts <= wordGroups.size() + 2) {
        this.finalMessage = "Great! Solved in " + this.totalAttempts + " attempts";
      } 
      else {
        this.finalMessage = "Good! Solved in " + this.totalAttempts + " attempts";
      }
      this.endOfWorld(this.finalMessage);
    }
  }
  
  // Returns the list of words corresponding to the selected indices
  ArrayList<String> getSelectedWords() {
    ArrayList<String> selectedWords = new ArrayList<String>();
    for (int index : this.selectedIndices) {
      selectedWords.add(this.currentGame.get(index));
    }
    return selectedWords;
  }
  
  // Returns the WordGroup that contains all selected words, or null if none match
  WordGroup findMatchingGroup(ArrayList<String> selectedWords) {
    for (WordGroup group : this.wordGroups) {
      if (group.words.containsAll(selectedWords) 
          && selectedWords.containsAll(group.words)) {
        return group;
      }
    }
    return new WordGroup(new ArrayList<String>(), "", Color.BLACK);
  }
  
  // Removes words from solved group (optional - could just recolor them)
  void removeSolvedWords(WordGroup solvedGroup) {
    this.currentGame.removeAll(solvedGroup.words);
  }
  
  // Called by bigBang to check if world should end
  public boolean worldEnded() {
    return gameHasEnded;
  }

  // Called by bigBang to get final message
  public String endWorldMessage() {
    return finalMessage;
  }
  
  // method to invoke big bang to run the game
  void runGame() {
    this.bigBang(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 0.1);
  }
}


// examples and test class
class ExamplesConnections {
  ConnectionsWorld c = new ConnectionsWorld();
  
  // test makeScene()
  void testMakeScene(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    WorldScene expectedScene = new WorldScene(800, 600);
    
    expectedScene.placeImageXY(
        new RectangleImage(800, 600, OutlineMode.SOLID, new Color(240, 240, 240)),
        800 / 2, 600 / 2);
    
    for (int i = 0; i < testWorld.currentGame.size(); i++) {
      testWorld.drawWordBox(expectedScene, i);
    }
    
    expectedScene.placeImageXY(
        new TextImage("Press: [Enter] Submit [Esc] Clear [S] Shuffle [D] Deselect", 
            14, Color.DARK_GRAY),
        800 / 2, 600 - 10);
    
    testWorld.drawGameStatus(expectedScene);
    
    t.checkExpect(testWorld.makeScene(), expectedScene);
    
    testWorld.selectedIndices.add(0);
    testWorld.selectedIndices.add(1);
    WorldScene selectedScene = testWorld.makeScene();
    t.checkExpect(selectedScene, testWorld.makeScene()); 
    
    testWorld.message = "TEST MESSAGE";
    testWorld.messageColor = Color.RED;
    WorldScene messageScene = testWorld.makeScene();
    t.checkExpect(messageScene, testWorld.makeScene());
  }
  
  // Test selectWordSet() method
  void testSelectWordSet(Tester t) {
    Random seededRand = new Random(42); 
    
    ConnectionsWorld testWorld = new ConnectionsWorld(seededRand);
    
    testWorld.selectWordSet();
    
    t.checkExpect(testWorld.wordGroups.size(), 4); 
    t.checkExpect(testWorld.currentGame.size(), 16); 
    
    ConnectionsWorld testWorld2 = new ConnectionsWorld(new Random(42));
    testWorld2.selectWordSet();
    t.checkExpect(testWorld2.wordGroups, testWorld.wordGroups);
    
    ArrayList<String> firstShuffle = new ArrayList<String>(testWorld.currentGame);
    t.checkExpect(firstShuffle.get(0), "ADIDAS"); 
    t.checkExpect(firstShuffle.get(1), "NIKE"); 
    t.checkExpect(firstShuffle.get(2), "PUMA"); 
    t.checkExpect(firstShuffle.get(3), "REEBOK"); 
  }
  
  // test shuffleCurrentGame() method
  void testShuffleCurrentGame(Tester t) {
    ArrayList<String> testWords = new ArrayList<String>(
        Arrays.asList("A", "B", "C", "D"));
    
    Random seededRand = new Random(42); 
    
    ConnectionsWorld testWorld = new ConnectionsWorld();
    testWorld.currentGame = new ArrayList<String>(testWords);
    testWorld.shuffleCurrentGame(seededRand);
    ArrayList<String> firstShuffle = new ArrayList<String>(testWorld.currentGame);
    
    ConnectionsWorld testWorld2 = new ConnectionsWorld();
    testWorld2.currentGame = new ArrayList<String>(testWords);
    testWorld2.shuffleCurrentGame(new Random(42));
    t.checkExpect(testWorld2.currentGame, firstShuffle);
    
    t.checkExpect(firstShuffle.get(0), "D"); 
    t.checkExpect(firstShuffle.get(1), "B"); 
    t.checkExpect(firstShuffle.get(2), "A"); 
    t.checkExpect(firstShuffle.get(3), "C"); 
  }
  
  
  // test drawWordBox method
  void testDrawWordBox(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    
    WordGroup testGroup = new WordGroup(
        new ArrayList<String>(Arrays.asList("TEST1", "TEST2", "TEST3", "TEST4")),
        "TEST CATEGORY", 
        Color.YELLOW
    );
    
    testWorld.wordGroups = new ArrayList<WordGroup>(Arrays.asList(testGroup));
    testWorld.currentGame = new ArrayList<String>(Arrays.asList(
        "TEST1", "TEST2", "TEST3", "TEST4"));
    testWorld.selectedIndices = new ArrayList<Integer>();
    testWorld.solvedGroups = new ArrayList<WordGroup>();
    
    WorldScene scene = new WorldScene(800, 600);
    
    testWorld.drawWordBox(scene, 0);
    
    WorldImage expectedBox = new OverlayImage(
        new TextImage("TEST1", 16, Color.BLACK),
        new OverlayImage(
            new RectangleImage(190, 140, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(192, 142, OutlineMode.OUTLINE, Color.BLACK)
        )
    );
    
    int x = (0 % 4) * 200 + 100;  
    int y = (0 / 4) * 150 + 75;   
    
    WorldScene expectedScene = new WorldScene(800, 600);
    expectedScene.placeImageXY(expectedBox, x, y);
    
    testWorld.selectedIndices.add(1);
    testWorld.drawWordBox(scene, 1);
    
    WorldImage selectedBox = new OverlayImage(
        new TextImage("TEST2", 16, new Color(70, 70, 200)),
        new OverlayImage(
            new RectangleImage(190, 140, OutlineMode.SOLID, new Color(230, 230, 255)),
            new RectangleImage(192, 142, OutlineMode.OUTLINE, new Color(70, 70, 200))
        )
    );
    expectedScene.placeImageXY(selectedBox, (1 % 4) * 200 + 100, (1 / 4) * 150 + 75);
    
    testWorld.solvedGroups.add(testGroup);
    testWorld.drawWordBox(scene, 2);
    
    WorldImage solvedBox = new OverlayImage(
        new TextImage("TEST3", 16, Color.YELLOW),
        new OverlayImage(
            new RectangleImage(190, 140, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(192, 142, OutlineMode.OUTLINE, Color.YELLOW)
        )
    );
    expectedScene.placeImageXY(solvedBox, (2 % 4) * 200 + 100, (2 / 4) * 150 + 75);
    
    t.checkExpect(scene, expectedScene);
    
    int col = 3 % 4;
    int row = 3 / 4;
    int xPos = col * 200 + 100;
    int yPos = row * 150 + 75;
    t.checkExpect(xPos, 700); 
    t.checkExpect(yPos, 75); 
  }
  
  // test getWordColor method
  void testGetWordColor(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    testWorld.currentGame = new ArrayList<String>(
        Arrays.asList("WORD1", "WORD2", "WORD3", "WORD4"));
    
    WordGroup yellowGroup = new WordGroup(
        new ArrayList<String>(Arrays.asList("WORD3")),
        "YELLOW GROUP", 
        Color.YELLOW
    );
    WordGroup greenGroup = new WordGroup(
        new ArrayList<String>(Arrays.asList("WORD4")),
        "GREEN GROUP",
        Color.GREEN
    );
    
    t.checkExpect(testWorld.getWordColor(0), Color.BLACK);
    t.checkExpect(testWorld.getWordColor(1), Color.BLACK);
    
    testWorld.selectedIndices.add(1); // Select WORD2
    t.checkExpect(testWorld.getWordColor(1), new Color(70, 70, 200));
    
    testWorld.solvedGroups.add(yellowGroup);
    t.checkExpect(testWorld.getWordColor(2), Color.YELLOW);
    
    testWorld.selectedIndices.add(2); 
    t.checkExpect(testWorld.getWordColor(2), new Color(70, 70, 200));
    testWorld.selectedIndices.remove((Integer) 2); 
    
    testWorld.solvedGroups.add(greenGroup);
    t.checkExpect(testWorld.getWordColor(3), Color.GREEN);
    t.checkExpect(testWorld.getWordColor(0), Color.BLACK);
    
    testWorld.selectedIndices.add(3); 
    t.checkExpect(testWorld.getWordColor(3), new Color(70, 70, 200));
    t.checkExpect(testWorld.getWordColor(-1), Color.BLACK);
    t.checkExpect(testWorld.getWordColor(100), Color.BLACK);
  }
  
  // test drawGameStatus method
  void testDrawGameStatus(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    testWorld.wordGroups = new ArrayList<WordGroup>();
    testWorld.solvedGroups = new ArrayList<WordGroup>();
    
    WorldScene scene = new WorldScene(800, 600);
    
    testWorld.drawGameStatus(scene);
    WorldScene emptyScene = new WorldScene(800, 600);
    t.checkExpect(scene.equals(emptyScene), false); 
    
    for (int i = 0; i < 4; i++) {
      testWorld.wordGroups.add(new WordGroup(new ArrayList<String>(), "GROUP" + i, Color.BLACK));
    }
    testWorld.solvedGroups.add(testWorld.wordGroups.get(0));
    testWorld.solvedGroups.add(testWorld.wordGroups.get(1));
    
    scene = new WorldScene(800, 600);
    testWorld.drawGameStatus(scene);
    t.checkExpect(scene.equals(emptyScene), false);
    
    testWorld.message = "TEST MESSAGE";
    testWorld.messageColor = Color.RED;
    scene = new WorldScene(800, 600);
    testWorld.drawGameStatus(scene);
    t.checkExpect(scene.equals(emptyScene), false);
    
    testWorld.solvedGroups.add(testWorld.wordGroups.get(2));
    testWorld.solvedGroups.add(testWorld.wordGroups.get(3));
    testWorld.message = ""; 
    scene = new WorldScene(800, 600);
    testWorld.drawGameStatus(scene);
    t.checkExpect(scene.equals(emptyScene), false);
    
    WorldScene expectedScene = new WorldScene(800, 600);
    WorldImage expectedStatus = new TextImage("Groups: 4/4", 18, Color.BLACK);
    expectedScene.placeImageXY(expectedStatus, 700, 570);
    
    WorldScene actualScene = new WorldScene(800, 600);
    testWorld.drawGameStatus(actualScene);
    
    t.checkExpect(actualScene, expectedScene);
  }
  
  // test initWords() 
  void testInitWords(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    testWorld.initWords();
    
    t.checkExpect(testWorld.allWordSets.size(), 5);
    
    for (ArrayList<WordGroup> set : testWorld.allWordSets) {
      t.checkExpect(set.size(), 4);
    }
    
    ArrayList<WordGroup> set1 = testWorld.allWordSets.get(0);
    t.checkExpect(set1.get(0).category, "WET WEATHER");
    t.checkExpect(set1.get(1).category, "NBA TEAMS");
    t.checkExpect(set1.get(2).category, "KEYBOARD KEYS");
    t.checkExpect(set1.get(3).category, "PALINDROMES");
    
    ArrayList<WordGroup> set2 = testWorld.allWordSets.get(1);
    t.checkExpect(set2.get(0).category, "FACIAL FEATURES");
    t.checkExpect(set2.get(1).category, "SYNONYMS FOR EAT");
    
    for (ArrayList<WordGroup> set : testWorld.allWordSets) {
      for (WordGroup group : set) {
        t.checkExpect(group.words.size(), 4); 
      }
    }
    
    WordGroup weatherGroup = set1.get(0);
    t.checkExpect(weatherGroup.words.contains("HAIL"), true);
    t.checkExpect(weatherGroup.words.contains("RAIN"), true);
    t.checkExpect(weatherGroup.words.contains("SLEET"), true);
    t.checkExpect(weatherGroup.words.contains("SNOW"), true);
    t.checkExpect(weatherGroup.groupColor, Color.YELLOW);
    
    ArrayList<WordGroup> set5 = testWorld.allWordSets.get(4);
    t.checkExpect(set5.get(0).category, "STREAMING SERVICES");
    t.checkExpect(set5.get(3).category, "CLUE CHARACTERS");
    t.checkExpect(set5.get(3).words.contains("GREEN"), true);
    t.checkExpect(set5.get(3).words.contains("MUSTARD"), true);
  }
  
  // Test findMaxMatchingWords() method
  void testFindMaxMatchingWords(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    
    WordGroup group1 = new WordGroup(
        new ArrayList<String>(Arrays.asList("A", "B", "C", "D")), 
        "Group1", Color.RED);
    WordGroup group2 = new WordGroup(
        new ArrayList<String>(Arrays.asList("W", "X", "Y", "Z")), 
        "Group2", Color.BLUE);
    WordGroup group3 = new WordGroup(
        new ArrayList<String>(Arrays.asList("A", "X", "C", "Z")), 
        "Group3", Color.GREEN);
    
    testWorld.wordGroups = new ArrayList<WordGroup>(Arrays.asList(group1, group2, group3));
    testWorld.solvedGroups = new ArrayList<WordGroup>();

    ArrayList<String> selection1 = new ArrayList<String>(Arrays.asList("A", "B", "C", "D"));
    t.checkExpect(testWorld.findMaxMatchingWords(selection1), 4);
    ArrayList<String> selection2 = new ArrayList<String>(Arrays.asList("A", "B", "C", "W"));
    t.checkExpect(testWorld.findMaxMatchingWords(selection2), 3);
    ArrayList<String> selection3 = new ArrayList<String>(Arrays.asList("A", "X", "M", "N"));
    t.checkExpect(testWorld.findMaxMatchingWords(selection3), 2);
    ArrayList<String> selection4 = new ArrayList<String>(Arrays.asList("M", "N", "O", "P"));
    t.checkExpect(testWorld.findMaxMatchingWords(selection4), 0);
    ArrayList<String> selection7 = new ArrayList<String>();
    t.checkExpect(testWorld.findMaxMatchingWords(selection7), 0);
    ArrayList<String> selection9 = new ArrayList<String>(Arrays.asList("A", "X", "Y", "D"));
    t.checkExpect(testWorld.findMaxMatchingWords(selection9), 2); 
  }
  
  // test checkGameEnd() method
  void testCheckGameEnd(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld(new Random());
    WordGroup group1 = new WordGroup(new ArrayList<String>(Arrays.asList("A", "B", "C", "D")), 
                                   "Group1", Color.RED);
    WordGroup group2 = new WordGroup(new ArrayList<String>(Arrays.asList("E", "F", "G", "H")), 
                                   "Group2", Color.BLUE);
    
    testWorld.wordGroups = new ArrayList<WordGroup>(Arrays.asList(group1, group2));
    testWorld.solvedGroups = new ArrayList<WordGroup>();
    testWorld.totalAttempts = 0;

    boolean beforeEnd = testWorld.gameHasEnded;
    testWorld.checkGameEnd();
    t.checkExpect(testWorld.gameHasEnded, beforeEnd);
    
    testWorld.solvedGroups.add(group1);
    testWorld.checkGameEnd();
    t.checkExpect(testWorld.gameHasEnded, false);
    
    testWorld.solvedGroups.add(group2);
    testWorld.totalAttempts = 2; 
    testWorld.checkGameEnd();
    t.checkExpect(testWorld.gameHasEnded, true);
    t.checkExpect(testWorld.finalMessage, "Perfect! Solved in 2 attempts");
    
    ConnectionsWorld testWorld2 = new ConnectionsWorld(new Random());
    testWorld2.wordGroups = new ArrayList<WordGroup>(Arrays.asList(group1, group2));
    testWorld2.solvedGroups = new ArrayList<WordGroup>(Arrays.asList(group1, group2));
    testWorld2.totalAttempts = 3; 
    testWorld2.checkGameEnd();
    t.checkExpect(testWorld2.gameHasEnded, true);
    t.checkExpect(testWorld2.finalMessage, "Great! Solved in 3 attempts");
    
    ConnectionsWorld testWorld3 = new ConnectionsWorld(new Random());
    testWorld3.wordGroups = new ArrayList<WordGroup>(Arrays.asList(group1, group2));
    testWorld3.solvedGroups = new ArrayList<WordGroup>(Arrays.asList(group1, group2));
    testWorld3.totalAttempts = 5; 
    testWorld3.checkGameEnd();
    t.checkExpect(testWorld3.gameHasEnded, true);
    t.checkExpect(testWorld3.finalMessage, "Good! Solved in 5 attempts");
  }
  
  // Test findMatchingGroup() method
  void testFindMatchingGroup(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    WordGroup group1 = new WordGroup(
        new ArrayList<String>(Arrays.asList("A", "B", "C", "D")), 
        "Group1", Color.RED);
    WordGroup group2 = new WordGroup(
        new ArrayList<String>(Arrays.asList("W", "X", "Y", "Z")), 
        "Group2", Color.BLUE);
    
    testWorld.wordGroups = new ArrayList<WordGroup>(Arrays.asList(group1, group2));

    ArrayList<String> selection1 = new ArrayList<String>(Arrays.asList("A", "B", "C", "D"));
    WordGroup result1 = testWorld.findMatchingGroup(selection1);
    t.checkExpect(result1.category.equals("Group1"), true);
    
    ArrayList<String> selection2 = new ArrayList<String>(Arrays.asList("W", "X", "Y", "Z"));
    WordGroup result2 = testWorld.findMatchingGroup(selection2);
    t.checkExpect(result2.category.equals("Group2"), true);
    
    ArrayList<String> selection3 = new ArrayList<String>(Arrays.asList("A", "B", "C", "X"));
    WordGroup result3 = testWorld.findMatchingGroup(selection3);
    t.checkExpect(result3.category.isEmpty(), true); 
    
    ArrayList<String> selection4 = new ArrayList<String>(Arrays.asList("M", "N", "O", "P"));
    WordGroup result4 = testWorld.findMatchingGroup(selection4);
    t.checkExpect(result4.category.isEmpty(), true);
    
    ArrayList<String> selection5 = new ArrayList<String>();
    WordGroup result5 = testWorld.findMatchingGroup(selection5);
    t.checkExpect(result5.category.isEmpty(), true);
    
    ArrayList<String> selection6 = new ArrayList<String>(Arrays.asList("D", "C", "B", "A"));
    WordGroup result6 = testWorld.findMatchingGroup(selection6);
    t.checkExpect(result6.category.equals("Group1"), true);
  }
  
  // Test removeSolvedWords() method
  void testRemoveSolvedWords(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld(new Random());
    ArrayList<String> words1 = new ArrayList<String>(Arrays.asList("A", "B", "C", "D"));
    ArrayList<String> words2 = new ArrayList<String>(Arrays.asList("W", "X", "Y", "Z"));
    
    WordGroup group1 = new WordGroup(new ArrayList<String>(words1), "Group1", Color.RED);
    WordGroup group2 = new WordGroup(new ArrayList<String>(words2), "Group2", Color.BLUE);
    
    testWorld.currentGame = new ArrayList<String>();
    testWorld.currentGame.addAll(words1);
    testWorld.currentGame.addAll(words2);
    testWorld.wordGroups = new ArrayList<WordGroup>(Arrays.asList(group1, group2));
    testWorld.solvedGroups = new ArrayList<WordGroup>();

    testWorld.solvedGroups.add(group1);
    testWorld.removeSolvedWords(group1);
    
    t.checkExpect(testWorld.currentGame.size(), 4); 
    t.checkExpect(testWorld.currentGame.contains("A"), false);
    t.checkExpect(testWorld.currentGame.contains("B"), false);
    t.checkExpect(testWorld.currentGame.contains("C"), false);
    t.checkExpect(testWorld.currentGame.contains("D"), false);
    t.checkExpect(testWorld.currentGame.contains("W"), true);
    t.checkExpect(testWorld.currentGame.contains("X"), true);
    t.checkExpect(testWorld.currentGame.contains("Y"), true);
    t.checkExpect(testWorld.currentGame.contains("Z"), true);

    testWorld.solvedGroups.add(group2);
    testWorld.removeSolvedWords(group2);
    
    t.checkExpect(testWorld.currentGame.size(), 0); 
    t.checkExpect(testWorld.currentGame.isEmpty(), true);

    testWorld.removeSolvedWords(group1); 
    t.checkExpect(testWorld.currentGame.isEmpty(), true);

    testWorld.currentGame = new ArrayList<String>(Arrays.asList("A", "W", "X", "D"));
    testWorld.removeSolvedWords(group1);
    
    t.checkExpect(testWorld.currentGame.size(), 2); 
    t.checkExpect(testWorld.currentGame.contains("W"), true);
    t.checkExpect(testWorld.currentGame.contains("X"), true);
  }
  
  // Test onMouseClicked() method
  void testOnMouseClicked(Tester t) {
    int WORD_BOX_HEIGHT = 150;
    int WORD_BOX_WIDTH = 200;
    ConnectionsWorld testWorld = new ConnectionsWorld(new Random());
    testWorld.currentGame = new ArrayList<String>(Arrays.asList(
        "WORD1", "WORD2", "WORD3", "WORD4",
        "WORD5", "WORD6", "WORD7", "WORD8",
        "WORD9", "WORD10", "WORD11", "WORD12",
        "WORD13", "WORD14", "WORD15", "WORD16"
    ));
    
    Posn pos1 = new Posn(WORD_BOX_WIDTH / 2, WORD_BOX_HEIGHT / 2); 
    testWorld.onMouseClicked(pos1);
    t.checkExpect(testWorld.selectedIndices.size(), 1);
    t.checkExpect(testWorld.selectedIndices.get(0), 0);
    t.checkExpect(testWorld.currentGame.get(testWorld.selectedIndices.get(0)), "WORD1");
    
    Posn pos2 = new Posn(WORD_BOX_WIDTH * 3 + WORD_BOX_WIDTH / 2, WORD_BOX_HEIGHT / 2);
    testWorld.onMouseClicked(pos2);
    t.checkExpect(testWorld.selectedIndices.size(), 2);
    t.checkExpect(testWorld.selectedIndices.get(1), 3);
    t.checkExpect(testWorld.currentGame.get(testWorld.selectedIndices.get(1)), "WORD4");
    
    Posn pos3 = new Posn(WORD_BOX_WIDTH * 3 + WORD_BOX_WIDTH / 2, 
                        WORD_BOX_HEIGHT * 3 + WORD_BOX_HEIGHT / 2);
    testWorld.onMouseClicked(pos3);
    t.checkExpect(testWorld.selectedIndices.size(), 3);
    t.checkExpect(testWorld.selectedIndices.get(2), 15);
    t.checkExpect(testWorld.currentGame.get(testWorld.selectedIndices.get(2)), "WORD16");
    
    Posn pos4 = new Posn(WORD_BOX_WIDTH + WORD_BOX_WIDTH / 2, WORD_BOX_HEIGHT / 2);
    testWorld.onMouseClicked(pos4);
    t.checkExpect(testWorld.selectedIndices.get(testWorld.selectedIndices.size() - 1), 1);
    
    Posn pos5 = new Posn(WORD_BOX_WIDTH * 1 + 5, WORD_BOX_HEIGHT * 1 + 5);
    testWorld.onMouseClicked(pos5);
    t.checkExpect(testWorld.selectedIndices.get(testWorld.selectedIndices.size() - 1), 1);
    
    testWorld.selectedIndices.clear();
    Posn pos6 = new Posn(WORD_BOX_WIDTH / 2, WORD_BOX_HEIGHT / 2);
    testWorld.onMouseClicked(pos6); 
    t.checkExpect(testWorld.selectedIndices.size(), 1);
    testWorld.onMouseClicked(pos6);
    t.checkExpect(testWorld.selectedIndices.size(), 0);
    
    testWorld.selectedIndices.clear();
    for (int i = 0; i < 5; i++) { 
      Posn pos = new Posn(WORD_BOX_WIDTH * (i % 4) + WORD_BOX_WIDTH / 2, 
          WORD_BOX_HEIGHT * (i / 4) + WORD_BOX_HEIGHT / 2);
      testWorld.onMouseClicked(pos);
    }
    t.checkExpect(testWorld.selectedIndices.size(), 4);
  }
  
  // test onKeyEvent
  void testOnKeyEvent(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld(new Random(42));
    
    WordGroup testGroup = new WordGroup(
        new ArrayList<String>(Arrays.asList("ADIDAS", "NIKE", "PUMA", "REEBOK")),
        "SNEAKER BRANDS", 
        Color.YELLOW
    );
    
    testWorld.wordGroups = new ArrayList<WordGroup>(Arrays.asList(testGroup));
    testWorld.currentGame = new ArrayList<String>(testGroup.words);
    testWorld.solvedGroups.clear();
    testWorld.remainingAttempts = 4;
    testWorld.totalAttempts = 0;

    testWorld.selectedIndices.clear();
    testWorld.onKeyEvent("enter");
    t.checkExpect(testWorld.message, "Select exactly 4 words");
    t.checkExpect(testWorld.messageColor, Color.RED);
    
    testWorld.selectedIndices.add(0);
    testWorld.selectedIndices.add(1);
    testWorld.onKeyEvent("escape");
    t.checkExpect(testWorld.selectedIndices.size(), 0);
    t.checkExpect(testWorld.message, "Selection cleared");
    
    ArrayList<String> originalOrder = new ArrayList<String>(testWorld.currentGame);
    testWorld.onKeyEvent("s");
    t.checkExpect(testWorld.message, "Words shuffled");
    t.checkExpect(testWorld.currentGame.equals(originalOrder), false);
    
    testWorld.selectedIndices.add(2);
    testWorld.selectedIndices.add(3);
    testWorld.onKeyEvent("d");
    t.checkExpect(testWorld.selectedIndices.size(), 0);
    t.checkExpect(testWorld.message, "Selection cleared");
    
    testWorld.currentGame = new ArrayList<String>(testGroup.words); 
    testWorld.selectedIndices = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
    testWorld.solvedGroups.clear();
    testWorld.remainingAttempts = 4;
    testWorld.totalAttempts = 0;
    
    testWorld.onKeyEvent("enter");
    t.checkExpect(testWorld.solvedGroups.size(), 1);
    t.checkExpect(testWorld.message, "Correct! SNEAKER BRANDS");
    t.checkExpect(testWorld.messageColor, Color.YELLOW);
    
    testWorld.currentGame = new ArrayList<String>(Arrays.asList(
        "ADIDAS", "NIKE", "PUMA", "REEBOK",
        "WORD5", "WORD6", "WORD7", "WORD8"   
    ));
    testWorld.selectedIndices = new ArrayList<Integer>(Arrays.asList(4, 5, 6, 7));
    testWorld.remainingAttempts = 4;
    testWorld.onKeyEvent("enter");
    t.checkExpect(testWorld.remainingAttempts, 3);
    t.checkExpect(testWorld.message, "Incorrect! 3 tries left");
    t.checkExpect(testWorld.messageColor, Color.RED);
    
    testWorld.selectedIndices = new ArrayList<Integer>(Arrays.asList(0, 1, 2));
    testWorld.onKeyEvent("enter");
    t.checkExpect(testWorld.message, "Select exactly 4 words");
    t.checkExpect(testWorld.messageColor, Color.RED);
    
    testWorld.message = "Previous message";
    testWorld.onKeyEvent("x");
    t.checkExpect(testWorld.message, "Previous message");
  }

  // test toggleSelection method
  void testToggleSelection(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    
    testWorld.toggleSelection(0);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(0)));
    
    testWorld.toggleSelection(1);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(0, 1)));
    
    testWorld.toggleSelection(2);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(0, 1, 2)));
    
    testWorld.toggleSelection(3);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
    
    testWorld.toggleSelection(4);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
    
    testWorld.toggleSelection(2);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(0, 1, 3)));
    
    testWorld.toggleSelection(2);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(0, 1, 3, 2)));
    
    testWorld.toggleSelection(2);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(0, 1, 3)));
    
    testWorld.selectedIndices.clear();
    testWorld.toggleSelection(5);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>(Arrays.asList(5)));
    testWorld.toggleSelection(5);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>());
    
    testWorld.toggleSelection(-1);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>());
    
    testWorld.toggleSelection(100);
    t.checkExpect(testWorld.selectedIndices, new ArrayList<Integer>());
  }
  
  // test getSelectedWords()
  void testGetSelectedWords(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    
    // Set up a test game with known words
    testWorld.currentGame = new ArrayList<String>(Arrays.asList(
        "APPLE", "BANANA", "CHERRY", "DATE",
        "EGGPLANT", "FIG", "GRAPE", "HONEYDEW"
    ));
    
    testWorld.selectedIndices.clear();
    t.checkExpect(testWorld.getSelectedWords(), new ArrayList<String>());
    
    testWorld.selectedIndices = new ArrayList<Integer>(Arrays.asList(0));
    t.checkExpect(testWorld.getSelectedWords(), 
                 new ArrayList<String>(Arrays.asList("APPLE")));
    
    testWorld.selectedIndices = new ArrayList<Integer>(Arrays.asList(1, 3, 5));
    t.checkExpect(testWorld.getSelectedWords(),
                 new ArrayList<String>(Arrays.asList("BANANA", "DATE", "FIG")));
    
    testWorld.selectedIndices = new ArrayList<Integer>(Arrays.asList(0, 2, 4, 6));
    t.checkExpect(testWorld.getSelectedWords(),
                 new ArrayList<String>(Arrays.asList("APPLE", "CHERRY", "EGGPLANT", "GRAPE")));
    
    testWorld.selectedIndices = new ArrayList<Integer>(Arrays.asList(3, 1, 7));
    t.checkExpect(testWorld.getSelectedWords(),
                 new ArrayList<String>(Arrays.asList("DATE", "BANANA", "HONEYDEW")));
    
    testWorld.selectedIndices = new ArrayList<Integer>(Arrays.asList(2, 2, 2));
    t.checkExpect(testWorld.getSelectedWords(),
                 new ArrayList<String>(Arrays.asList("CHERRY", "CHERRY", "CHERRY")));
  }
  
  // test getWordIndexAt method
  void testGetWordIndexAt(Tester t) {
    ConnectionsWorld testWorld = new ConnectionsWorld();
    testWorld.currentGame = new ArrayList<String>(Collections.nCopies(16, "WORD"));
    
    t.checkExpect(testWorld.getWordIndexAt(10, 10), 0);    
    t.checkExpect(testWorld.getWordIndexAt(100, 75), 0);   
    t.checkExpect(testWorld.getWordIndexAt(199, 149), 0);
    t.checkExpect(testWorld.getWordIndexAt(210, 160), 5);  
    t.checkExpect(testWorld.getWordIndexAt(200, 150), 5); 
    t.checkExpect(testWorld.getWordIndexAt(399, 299), 5);
    t.checkExpect(testWorld.getWordIndexAt(-1, -1), -1);    
    t.checkExpect(testWorld.getWordIndexAt(800, 600), -1);  
    t.checkExpect(testWorld.getWordIndexAt(0, 0), 0);
    t.checkExpect(testWorld.getWordIndexAt(200, 0), 1);    
    t.checkExpect(testWorld.getWordIndexAt(0, 150), 4);     
    t.checkExpect(testWorld.getWordIndexAt(200, 150), 5);
    testWorld.currentGame = new ArrayList<String>(Arrays.asList("A", "B", "C"));
    t.checkExpect(testWorld.getWordIndexAt(100, 75), 0);    
    t.checkExpect(testWorld.getWordIndexAt(500, 75), 2);
    t.checkExpect(testWorld.getWordIndexAt(150, 100), 0);   
    t.checkExpect(testWorld.getWordIndexAt(250, 175), -1);
    t.checkExpect(testWorld.getWordIndexAt(199, 150), -1);   
    t.checkExpect(testWorld.getWordIndexAt(200, 149), 1);  
  }

  // test runGame()
  void testRunGame(Tester t) {
    c.runGame();
  }
}