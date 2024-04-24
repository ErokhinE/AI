from random import randint, random
import os


# Class for word in crossword it has name coordinates x and y of first symbol and orientation
class Word:
    def __init__(self, word, xx, yy, oriental):
        self.word = word
        self.x = xx
        self.y = yy
        self.oriental = oriental


# Function for linear(string representation) of crossword
def print_crossword_in_linear_form(crossword):
    for word in crossword:
        print(word.word, word.x, word.y, word.oriental)


def print_crossword_in_linear_form_without_word(crossword):
    for word in crossword:
        print(word.x, word.y, word.oriental)


# Function for graphic representation of crossword
def print_crossword_in_grid(crossword):
    cr = make_crossword_in_grid_form(crossword)
    for i in cr:
        for i2 in i:
            print(i2, end=' ')
        print()


# Make crossword from linear to grid form
def make_crossword_in_grid_form(crossword):
    cr = [['.' for _ in range(20)] for _ in range(20)]
    for word in crossword:
        x = word.x
        y = word.y
        if word.oriental == 'horizontal':
            for letter in word.word:
                cr[y][x] = letter
                x = x + 1
        else:
            for letter in word.word:
                cr[y][x] = letter
                y = y + 1
    return cr


# Function for taking word from file

def input_from(file):
    words_read = open(file, "r")
    words = []
    for word in words_read:
        words.append(word.rstrip())
    words.sort(key=len, reverse=True)
    return words


# Function for making initial population
def make_start_crosswords(words, population):
    initial = []
    for i in range(population):
        crossword = []
        for word in words:
            crossword.append(place_somehow(word))
        initial.append(crossword)
    return initial


# Take word and put it randomly with random orientation
def place_somehow(word):
    oriental = randint(0, 1)
    if oriental == 0:
        oriental_of_word = 'horizontal'
        x = randint(0, 20 - len(word))
        y = randint(0, 19)
        word_to_place = Word(word, x, y, oriental_of_word)
        return word_to_place
    else:
        oriental_of_word = 'vertical'
        x = randint(0, 19)
        y = randint(0, 20 - len(word))
        word_to_place = Word(word, x, y, oriental_of_word)
        return word_to_place


# Crossover function take two crossword and make new crossword randomly take parts from parent1 and parent2
def crossover(parent1, parent2):
    children = []
    par1 = parent1.copy()
    par2 = parent2.copy()
    for i in range(len(par1)):
        a = randint(0, 1)
        if a == 0:
            children.append(par1[i])
        else:
            children.append(par2[i])
    return children


# Mutation function take a crossword and with some probability every word can be changed somehow
def mutation(crossword):
    crossword_new = []
    for word in crossword:
        if random() <= 0.25:
            if word.oriental == "horizontal":
                crossword_new.append(Word(word.word, randint(0, 19), randint(0, 20 - len(word.word)), "vertical"))
            else:
                crossword_new.append(Word(word.word, randint(0, 20 - len(word.word)), randint(0, 19), "horizontal"))
        else:
            crossword_new.append(word)
    return crossword_new


# Fitness function for evaluation of crossword. It used to make new better generation
def fitness(cr, crossword):
    fitness_number = 0
    list_of_horizontal = []
    list_of_vertical = []
    for word in crossword:
        # Divide words according to orientation
        if word.oriental == 'horizontal':
            list_of_horizontal.append(word)
        else:
            list_of_vertical.append(word)

    # First: check that words intersects with one letter
    for hor_word in list_of_horizontal:
        for ver_word in list_of_vertical:
            if (ver_word.y <= hor_word.y <= ver_word.y + len(ver_word.word) - 1) and (
                    hor_word.x <= ver_word.x <= hor_word.x + len(hor_word.word) - 1):
                if hor_word.word[ver_word.x - hor_word.x] != ver_word.word[hor_word.y - ver_word.y]:
                    fitness_number += 50

    # Second check: words can not stand nearby(parallel ways)
    for hor_word_index in range(len(list_of_horizontal) - 1):
        for hor_word_index1 in range(1, len(list_of_horizontal)):
            if hor_word_index != hor_word_index1:
                if (((list_of_horizontal[hor_word_index].y + 1 == list_of_horizontal[hor_word_index1].y) or
                     (list_of_horizontal[hor_word_index].y - 1 == list_of_horizontal[hor_word_index1].y)) and
                        ((list_of_horizontal[hor_word_index].x <= list_of_horizontal[hor_word_index1].x <=
                          list_of_horizontal[hor_word_index].x +
                          len(list_of_horizontal[hor_word_index].word)) or
                         (list_of_horizontal[hor_word_index1].x <= list_of_horizontal[hor_word_index].x <=
                          list_of_horizontal[hor_word_index1].x +
                          len(list_of_horizontal[hor_word_index1].word)))):
                    fitness_number += 500

    for ver_word_index in range(len(list_of_vertical) - 1):
        for ver_word_index1 in range(1, len(list_of_vertical)):
            if ver_word_index != ver_word_index1:
                if (((list_of_vertical[ver_word_index].x + 1 == list_of_vertical[ver_word_index1].x) or
                     (list_of_vertical[ver_word_index].x - 1 == list_of_vertical[ver_word_index1].x)) and
                        ((list_of_vertical[ver_word_index].y <= list_of_vertical[ver_word_index1].y <= list_of_vertical[
                            ver_word_index].y +
                          len(list_of_vertical[ver_word_index].word)) or
                         (list_of_vertical[ver_word_index1].y <= list_of_vertical[ver_word_index].y <= list_of_vertical[
                             ver_word_index1].y + len(list_of_vertical[ver_word_index1].word)))):
                    fitness_number += 500

    # Third check words can not stand nearby(T ways perpendicular)
    for hor_word in list_of_horizontal:
        for ver_word in list_of_vertical:
            if (((ver_word.y == hor_word.y + 1) or (ver_word.y + len(ver_word.word) == hor_word.y)) and
                    (hor_word.x <= ver_word.x <= hor_word.x + len(hor_word.word))):
                fitness_number += 250
            if (((ver_word.x + 1 == hor_word.x) or (ver_word.x == hor_word.x + len(hor_word.word))) and
                    (ver_word.y <= hor_word.y <= ver_word.y + len(ver_word.word))):
                fitness_number += 250

    # Fourth check word can not go word after word(---- ---- without space in a middle)
    for hor_word_index in range(len(list_of_horizontal) - 1):
        for hor_word_index1 in range(1, len(list_of_horizontal)):
            if hor_word_index != hor_word_index1:
                if ((list_of_horizontal[hor_word_index].y == list_of_horizontal[hor_word_index1].y) and
                        ((list_of_horizontal[hor_word_index].x <= list_of_horizontal[hor_word_index1].x <=
                          list_of_horizontal[hor_word_index].x +
                          len(list_of_horizontal[hor_word_index].word)) or
                         (list_of_horizontal[hor_word_index1].x <= list_of_horizontal[hor_word_index].x <=
                          list_of_horizontal[hor_word_index1].x +
                          len(list_of_horizontal[hor_word_index1].word)))):
                    fitness_number += 500

    for ver_word_index in range(len(list_of_vertical) - 1):
        for ver_word_index1 in range(1, len(list_of_vertical)):
            if ver_word_index != ver_word_index1:
                if ((list_of_vertical[ver_word_index].x == list_of_vertical[ver_word_index1].x) and
                        ((list_of_vertical[ver_word_index].y <= list_of_vertical[ver_word_index1].y <= list_of_vertical[
                            ver_word_index].y +
                          len(list_of_vertical[ver_word_index].word)) or
                         (list_of_vertical[ver_word_index1].y <= list_of_vertical[ver_word_index].y <= list_of_vertical[
                             ver_word_index1].y + len(list_of_vertical[ver_word_index1].word)))):
                    fitness_number += 500

    # Fifth check that all words intersects with one or more words
    for word in crossword:
        intersection = False
        for word1 in crossword:
            if word.word != word1.word:
                if word.oriental != word1.oriental:
                    if word1.oriental == 'horizontal':
                        if ((word.y <= word1.y < word.y + len(word.word)) and
                                (word1.x <= word.x <= word1.x + len(word1.word) - 1)):
                            intersection = True
                    else:
                        if ((word.x <= word1.x < word.x + len(word.word)) and
                                (word1.y <= word.y <= word1.y + len(word1.word) - 1)):
                            intersection = True
        if not intersection:
            fitness_number += 125

    # Sixth check that all words intersects and make one union
    vis = [[False] * 20 for _ in range(20)]
    connection = 0
    for i in range(20):
        for j in range(20):
            if cr[i][j] != '.' and (not vis[i][j]):
                dfs(cr, vis, i, j)
                connection += 1

    fitness_number += 1000 * (connection - 1)

    return fitness_number


# DFS function to make go through all possible variants in grid crossword
def dfs(crossword, visited, x, y):
    if crossword[x][y] != '.' and (not visited[x][y]):
        visited[x][y] = True
        if x - 1 >= 0 and crossword[x - 1][y] != '.':
            dfs(crossword, visited, x - 1, y)
        if x + 1 <= 19 and crossword[x + 1][y] != '.':
            dfs(crossword, visited, x + 1, y)
        if y - 1 >= 0 and crossword[x][y - 1] != '.':
            dfs(crossword, visited, x, y - 1)
        if y + 1 <= 19 and crossword[x][y + 1] != '.':
            dfs(crossword, visited, x, y + 1)


# main function
def main():
    my_path = "students/ErokhinEvgenii/"
    output_path = f'{my_path}outputs'
    input_path = f'{my_path}inputs'
    os.mkdir(output_path)
    input_file = os.listdir(input_path)
    for f in input_file:
        # Number of each population
        population = 20
        # Best fitness to stop evolution
        best_fitness = 0
        need_to_continue = True
        # Make initial population
        file = f'{input_path}/{f}'
        initial_crosswords = make_start_crosswords(input_from(file), population)
        counter = 0

        # Loop for making evolution
        while need_to_continue:
            old_best_elem = initial_crosswords[0]
            parents = [obj.copy() for obj in initial_crosswords]
            new_generation_of_crosswords_children = []

            # Make new children from parents
            for crossword in range(population - 1):
                for crossword1 in range(crossword + 1, population):
                    children = crossover(parents[crossword], parents[crossword1])
                    new_generation_of_crosswords_children.append(mutation(children))

            # Add children to parents
            initial_crosswords = new_generation_of_crosswords_children + parents

            # Sort our parents+children according to fitness function
            initial_crosswords.sort(key=lambda x: fitness(make_crossword_in_grid_form(x), x))

            # Keep the best crosswords in size of population it is our new generation make algorithm again
            initial_crosswords = initial_crosswords[:int(population)]

            if old_best_elem == initial_crosswords[0]:
                counter = counter + 1
            else:
                counter = 0
            # If we go deep and have no evolution(Best element in the population not decreasing) make new population
            if counter == 300:
                initial_crosswords = make_start_crosswords(input_from(file), population)
                counter = 0

            if fitness(make_crossword_in_grid_form(initial_crosswords[0]), initial_crosswords[0]) == best_fitness:
                parse_number = f[5:-4]
                with open(output_path + "/" + "output" + parse_number + ".txt", 'w') as output_file:
                    for word in initial_crosswords[0]:
                        a=0
                        if word.oriental =='vertical':
                            a = 1
                        output = f"{word.y} {word.x} {a}\n"
                        output_file.write(output)
                break


if __name__ == '__main__':
    main()
