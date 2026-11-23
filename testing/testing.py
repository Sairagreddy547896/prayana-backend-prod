from urllib.request import urlopen
from html.parser import HTMLParser

class TableParser(HTMLParser):
    def __init__(self):
        super().__init__()
        self.rows = []
        self.current_row = []
        self.current_cell = ""
        self.in_td = False

    def handle_starttag(self, tag, attrs):
        if tag == "tr":
            self.current_row = []

        elif tag == "td":
            self.in_td = True
            self.current_cell = ""

    def handle_endtag(self, tag):
        if tag == "td":
            self.in_td = False
            self.current_row.append(self.current_cell.strip())

        elif tag == "tr":
            if self.current_row:
                self.rows.append(self.current_row)

    def handle_data(self, data):
        if self.in_td:
            self.current_cell += data


def print_secret_message(url):
    with urlopen(url) as response:
        html = response.read().decode("utf-8")

    parser = TableParser()
    parser.feed(html)
    points = []

    for row in parser.rows[1:]:
        if len(row) >= 3:
            x = int(row[0])
            character = row[1]
            y = int(row[2])

            points.append((x, y, character))

    if not points:
        return

    max_x = max(x for x, y, character in points)
    max_y = max(y for x, y, character in points)

    grid = [
        [" "] * (max_x + 1)
        for _ in range(max_y + 1)
    ]

    for x, y, character in points:
        grid[y][x] = character

    for y in range(max_y, -1, -1):
        print("".join(grid[y]))

print_secret_message("https://docs.google.com/document/d/e/2PACX-1vSvM5gDlNvt7npYHhp_XfsJvuntUhq184By5xO_pA4b_gCWeXb6dM6ZxwN8rE6S4ghUsCj2VKR21oEP/pub")