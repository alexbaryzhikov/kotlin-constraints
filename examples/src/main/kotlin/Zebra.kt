import com.alexb.constraints.Problem
import com.alexb.constraints.core.constraints.AllDifferentConstraint
import kotlin.system.measureTimeMillis

/*
The following version of the puzzle appeared in Life International in 1962:

There are five houses.
The Englishman lives in the red house.
The Spaniard owns the dog.
Coffee is drunk in the green house.
The Ukrainian drinks tea.
The green house is immediately to the right of the ivory house.
The Old Gold smoker owns snails.
Kools are smoked in the yellow house.
Milk is drunk in the middle house.
The Norwegian lives in the first house.
The man who smokes Chesterfields lives in the house next to the man with the fox.
Kools are smoked in the house next to the house where the horse is kept.
The Lucky Strike smoker drinks orange juice.
The Japanese smokes Parliaments.
The Norwegian lives next to the blue house.
Now, who drinks water? Who owns the zebra?

In the interest of clarity, it must be added that each of the five houses is
painted a different color, and their inhabitants are of different national
extractions, own different pets, drink different beverages and smoke different
brands of American cigarets [sic]. One other thing: in statement 6, right means
your right.
*/

fun zebra(): List<Map<String, String>> {
    val problem = Problem<String, String>().apply {
        for (i in 1..5) {
            addVariable(
                "COL$i",
                listOf("red", "green", "ivory", "yellow", "blue")
            )
            addVariable(
                "NAT$i",
                listOf("englishman", "spaniard", "ukrainian", "japanese", "norwegian")
            )
            addVariable(
                "PET$i",
                listOf("dog", "snails", "fox", "horse", "zebra")
            )
            addVariable(
                "DRI$i",
                listOf("coffee", "tea", "milk", "orange juice", "water")
            )
            addVariable(
                "CIG$i",
                listOf("old gold", "kools", "chesterfields", "lucky strike", "parliaments")
            )
        }

        addConstraint(AllDifferentConstraint(), (1..5).map { "COL$it" })
        addConstraint(AllDifferentConstraint(), (1..5).map { "NAT$it" })
        addConstraint(AllDifferentConstraint(), (1..5).map { "PET$it" })
        addConstraint(AllDifferentConstraint(), (1..5).map { "DRI$it" })
        addConstraint(AllDifferentConstraint(), (1..5).map { "CIG$it" })

        for (i in (1..5)) {
            // The Englishman lives in the red house.
            addConstraint(
                { nat, col -> nat != "englishman" || col == "red" },
                listOf("NAT$i", "COL$i")
            )
            // The Spaniard owns the dog.
            addConstraint(
                { nat, pet -> nat != "spaniard" || pet == "dog" },
                listOf("NAT$i", "PET$i")
            )
            // Coffee is drunk in the green house.
            addConstraint(
                { dri, col -> dri != "coffee" || col == "green" },
                listOf("DRI$i", "COL$i")
            )
            // The Ukrainian drinks tea.
            addConstraint(
                { nat, dri -> nat != "ukrainian" || dri == "tea" },
                listOf("NAT$i", "DRI$i")
            )
            // The green house is immediately to the right of the ivory house.
            if (i < 5) {
                addConstraint(
                    { colA, colB -> colA != "ivory" || colB == "green" },
                    listOf("COL$i", "COL${i + 1}")
                )
            } else {
                addConstraint({ col -> col != "ivory" }, "COL$i")
            }
            // The Old Gold smoker owns snails.
            addConstraint(
                { cig, pet -> cig != "old gold" || pet == "snails" },
                listOf("CIG$i", "PET$i")
            )
            // Kools are smoked in the yellow house.
            addConstraint(
                { cig, col -> cig != "kools" || col == "yellow" },
                listOf("CIG$i", "COL$i")
            )
            // Milk is drunk in the middle house.
            if (i == 3) {
                addConstraint({ dri -> dri == "milk" }, "DRI$i")
            }
            // The Norwegian lives in the first house.
            if (i == 1) {
                addConstraint({ nat -> nat == "norwegian" }, "NAT$i")
            }
            // The man who smokes Chesterfields lives in the house next to the man with the fox.
            if (i in 2..4) {
                addConstraint(
                    { pet, cigA, cigB ->
                        pet != "fox" || cigA == "chesterfields" || cigB == "chesterfields"
                    },
                    listOf("PET$i", "CIG${i - 1}", "CIG${i + 1}")
                )
            } else {
                addConstraint(
                    { pet, cig -> pet != "fox" || cig == "chesterfields" },
                    listOf("PET$i", "CIG${if (i == 1) 2 else 4}")
                )
            }
            // Kools are smoked in the house next to the house where the horse is kept.
            if (i in 2..4) {
                addConstraint(
                    { pet, cigA, cigB ->
                        pet != "horse" || cigA == "kools" || cigB == "kools"
                    },
                    listOf("PET$i", "CIG${i - 1}", "CIG${i + 1}")
                )
            } else {
                addConstraint(
                    { pet, cig -> pet != "horse" || cig == "kools" },
                    listOf("PET$i", "CIG${if (i == 1) 2 else 4}")
                )
            }
            // The Lucky Strike smoker drinks orange juice.
            addConstraint(
                { cig, dri -> cig != "lucky strike" || dri == "orange juice" },
                listOf("CIG$i", "DRI$i")
            )
            // The Japanese smokes Parliaments.
            addConstraint(
                { nat, cig -> nat != "japanese" || cig == "parliaments" },
                listOf("NAT$i", "CIG$i")
            )
            // The Norwegian lives next to the blue house.
            if (i in 2..4) {
                addConstraint(
                    { col, natA, natB ->
                        col != "blue" || natA == "norwegian" || natB == "norwegian"
                    },
                    listOf("COL$i", "NAT${i - 1}", "NAT${i + 1}")
                )
            } else {
                addConstraint(
                    { col, nat -> col != "blue" || nat == "norwegian" },
                    listOf("COL$i", "NAT${if (i == 1) 2 else 4}")
                )
            }
        }
    }
    return problem.getSolutions()
}

fun showSolution(solution: Map<String, String>) {
    for (i in 1..5) {
        println("House $i")
        println("-------")
        println("COL: ${solution["COL$i"]}")
        println("NAT: ${solution["NAT$i"]}")
        println("PET: ${solution["PET$i"]}")
        println("DRI: ${solution["DRI$i"]}")
        println("CIG: ${solution["CIG$i"]}")
        println()
    }
}

fun main() {
    lateinit var solutions: List<Map<String, String>>
    val t = measureTimeMillis { solutions = zebra() }
    println("Found ${solutions.size} solution(s) in $t ms\n")
    for ((i, solution) in solutions.withIndex()) {
        println("===============")
        println("Solution ${i + 1}")
        println("===============\n")
        showSolution(solution)
    }
}
