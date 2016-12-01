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

(defn vec-from-board [board]
  (into [] (flatten board))
  )

(defn board-from-vec [flat-board-vec]
  (let [[e0 e1 e2 e3 e4 e5 e6 e7 e8] flat-board-vec]
   [[e0 e1 e2]
    [e3 e4 e5]
    [e6 e7 e8]
    ]
  ))

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

(defn same-board? [board1 board2]
  (or
    (= board1 board2)
    (= board1 (rotate-clockwise board2))
    (= board1 (rotate-clockwise (rotate-clockwise board2)))
    (= board1 (rotate-counter-clockwise board2))
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

(defn get-coords-for-item [board item]
  (map first (filter #(= item (second %)) (map-indexed vector (flatten board))))
)

(defn next-moves-for-player [board player]
  (map #(assoc (vec-from-board board) % player) (get-coords-for-item board empty-square))
  )

(defn dedupe-board-vec-list [board-vec-list]
  (cond 
    (= 0 (count board-vec-list)) nil
    (= 1 (count board-vec-list)) board-vec-list
    :else 
    (cons (first board-vec-list)
        (dedupe-board-vec-list 
          (filter #((complement same-board?) (board-from-vec (first board-vec-list)) (board-from-vec %)) (rest board-vec-list))
          )
        )
  ))

(defn unique-next-moves-for-player [board player]
  (dedupe-board-vec-list (next-moves-for-player board player))
  )

(defn add-next-move-children [board player]
  (cons (vec-from-board board) (vector (unique-next-moves-for-player board player)))
  )

(defn pack-seq [sq] 
  (loop [in sq out [] pack '()] 
    (if (empty? in) 
      (conj out pack)
      (if (or (contains? (set pack) (first in)) (empty? pack))
        (recur (rest in) 
               out 
               (cons (first in) pack)) 
        (recur (rest in) 
               (conj out pack) 
               (list(first in)))
        )
      )
    ))

(defn drop-nth [sq n]
    (if (< (count sq) n)
          sq
          (flatten (cons (take (dec n) sq) (drop-nth (drop n sq) n )))
          )
    )

(defn rep-seq [sq n]
  (apply concat 
         ( (fn rp-sq [sq n]
  (if (empty? sq)
    sq
    (cons (take n (iterate (fn x [y] y) (first sq))) (rp-sq (rest sq) n))
    )) sq n)
         ))

(defn fib [n]
  (map (fn fb [n]
    (if (<= n 2)
          1
          (+ (fb (- n 1)) (fb (- n 2)))
                    )
        )
       (range 1 (+ n 1)))
  )

(defn truthy [x & next]
    (cond
         (not (or x next)) false
         (and x next ) false
         :else true
         )
    )

(defn test-args [& args]
  (println args)
  )

;(defn gcd [a b]
;  (if (= b 0)
;    a
;    (gcd b, (mod a b))
;    ))
(defn lcm [x y]
  (/ (* x y)
((fn gcd [a b]
  (if (= b 0)
    a
    (gcd b, (mod a b))
    )) x y))
     )

(fn lcm-multi [& sq] 
  (reduce 
    (fn lcm [x y] 
      (/ (* x y) 
         ((fn gcd [a b] 
            (if (= b 0) 
              a 
              (gcd b, (mod a b))
              )) x y))
      ) sq))








;(defn num-open-spots [board]
;  (let [[row0 row1 row2] board]

