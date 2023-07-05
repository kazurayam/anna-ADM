from PIL import Image

im = Image.open('c967b7713749af8c0d1ca73304c7ea6b2b8686e5_2_208x998.jpeg')
left = 0
upper = 0
width = 208
height = 200
im.crop((left, upper+height*0, width, height+height*0)).save('part0.jpg', quality=95)
im.crop((left, upper+height*1, width, height+height*1)).save('part1.jpg', quality=95)
im.crop((left, upper+height*2, width, height+height*2)).save('part2.jpg', quality=95)
im.crop((left, upper+height*3, width, height+height*3)).save('part3.jpg', quality=95)
im.crop((left, upper+height*4, width, height+height*4)).save('part4.jpg', quality=95)
