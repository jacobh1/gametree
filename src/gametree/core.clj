(ns gametree.core)

;;; This is an incorrect implementation, such as might be written by
;;; someone who was used to a Lisp in which an empty list is equal to
;;; nil.
(defn first-element [sequence default]
  (if (nil? sequence)
    default
    (first sequence)))


(def X \X)
(def O \O)
(def e \space)
(def empty-square e) 

(def empty-board [[e e e]
                  [e e e]
                  [e e e]])

(def example-board [[X O e]
                    [O O e]
                    [X e X]])

(def labeled-board [[1 2 3]
                    [4 5 6]
                    [7 8 9]])

(defn row-values [board coord]
  (let [[row col] coord] 
    (apply sorted-set (get board row))
  ))

(defn rotate-clockwise [board]
  (let [[row0 row1 row2] board]
    [ [(get row2 0) (get row1 0) (get row0 0)]
      [(get row2 1) (get row1 1) (get row0 1)]
      [(get row2 2) (get row1 2) (get row0 2)]
     ]
    ))

(defn rotate-counter-clockwise [board]
  (let [[row0 row1 row2] board]
    [ [(get row0 2) (get row1 2) (get row2 2)]
      [(get row0 1) (get row1 1) (get row2 1)]
      [(get row0 0) (get row1 0) (get row2 0)]
     ]
    ))

(defn has-row-winner? [board]
  (let [[row0 row1 row2] board]
    (or 
      (= [X X X] row0)
      (= [O O O] row0)
      (= [X X X] row1)
      (= [O O O] row1)
      (= [X X X] row2)
      (= [O O O] row2)
      )
    )
  )
(defn has-diagonal-winner? [board]
  (let [[row0 row1 row2] board]
    (or
      (and (= X (get row0 0)) 
           (= X (get row1 1)) 
           (= X (get row2 2)))
      (and (= O (get row0 0)) 
           (= O (get row1 1)) 
           (= O (get row2 2)))
      )
    )
  )

(defn has-winner? [board]
  (or 
    (has-row-winner? board)
    (has-row-winner? (rotate-clockwise board))
    (has-diagonal-winner? board)
    (has-diagonal-winner? (rotate-clockwise board))
    )
  )

(defn open-spots? [board]
  (contains? (set (flatten board)) empty-square)
  )

(defn board-full? [board]
  (not (open-spots? board))
  )


(defn game-over? [board]
  (or (has-winner? board)
      (board-full? board)
      ))

;(defn num-open-spots [board]
;  (let [[row0 row1 row2] board]


