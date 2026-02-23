import json
import os

template = "\\u0926\\u093f\\u0928 {d}: \\u0936\\u093e\\u0902\\u0924\\u093f \\u0938\\u0947 \\u091c\\u092a \\u0915\\u0930\\u0947\\u0902\\u0964"
hindi_template = template.encode("utf-8").decode("unicode_escape")

quotes = []
for day in range(1, 366):
    quotes.append(
        {
            "day": day,
            "hindi": hindi_template.format(d=day),
            "english": f"Day {day}: Chant with calmness.",
        }
    )

path = os.path.join("app", "src", "main", "assets", "quotes.json")
os.makedirs(os.path.dirname(path), exist_ok=True)
with open(path, "w", encoding="utf-8") as f:
    json.dump(quotes, f, ensure_ascii=True, indent=2)
